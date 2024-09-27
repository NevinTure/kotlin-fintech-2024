package edu.kotlin.utils

interface Element {
    fun render(builder: StringBuilder, indent: String)
}

class TextElement(var text: String) : Element {
    override fun render(builder: StringBuilder, indent: String) {
        builder.append("$indent$text${System.lineSeparator()}")
    }
}

@DslMarker
annotation class TagMarker

@TagMarker
abstract class Tag (val name: String) : Element {
    val children = arrayListOf<Element>()
    val attributes = hashMapOf<String, String>()

    protected fun <T : Element> initTag(tag: T, init: T.() -> Unit): T {
        tag.init()
        children.add(tag)
        return tag
    }

    override fun render(builder: StringBuilder, indent: String) {
        val separator = System.lineSeparator()
        builder.append("$indent<$name${renderAttributes()}>$separator")
        for (c in children) {
            c.render(builder, "$indent  ")
        }
        builder.append("$indent</$name>$separator")
    }

    private fun renderAttributes(): String {
        val builder: StringBuilder = StringBuilder()
        for ((attr, value) in attributes) {
            builder.append(" $attr=\"$value\"")
        }
        return builder.toString()
    }

    override fun toString(): String {
        val builder = StringBuilder()
        render(builder, "")
        return builder.toString()
    }
}

abstract class TagWithText(name: String) : Tag(name) {
    operator fun String.unaryPlus() {
        children.add(TextElement("$this</br>"))
    }
}

class Root: TagWithText("root") {

    override fun render(builder: StringBuilder, indent: String) {
        val separator = System.lineSeparator()
        builder.append("$indent<html>$separator")
        builder.append("$indent<head><meta charset=\"utf-8\"></head>$separator")
        builder.append("$indent<body>$separator")
        for (c in children) {
            c.render(builder, "$indent  ")
        }
        builder.append("$indent</body>$separator")
        builder.append("$indent</html>$separator")
    }
    fun text(init: Text.() -> Unit) = initTag(Text(), init)
    fun header(level: Int, init: Header.() -> Unit) {
        val header = initTag(Header(), init)
        header.level = level
    }

}

class Header : TagWithText("header") {

    var level: Int
        get() = attributes["level"]!!.toInt()
        set(value) {
            attributes["level"] = value.toString()
        }

    override fun render(builder: StringBuilder, indent: String) {
        val separator = System.lineSeparator()
        builder.append("$indent<h$level>$separator")
        for (c in children) {
            c.render(builder, "$indent  ")
        }
        builder.append("$indent</h$level>$separator")
    }
}


abstract class BodyTag(name: String) : TagWithText(name) {
    fun b(init: B.() -> Unit) = initTag(B(), init)
    fun p(init: P.() -> Unit) = initTag(P(), init)
    fun a(href: String, init: A.() -> Unit) {
        val a = initTag(A(), init)
        a.href = href
    }
}

class Text: BodyTag("div")
class B : BodyTag("b")
class P : BodyTag("p")

class A : BodyTag("a") {
    var href: String
        get() = attributes["href"]!!
        set(value) {
            attributes["href"] = value
        }
}

fun ownDSL(init: Root.() -> Unit): Root {
    val root = Root()
    root.init()
    return root
}
