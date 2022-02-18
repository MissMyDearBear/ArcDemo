package com.bear.arcdemo.showcode

/**
 * 这样写的缺点
 * 1. Map中含有对应存储对象的强引用
 * 2. 采用hashMap 线程不安全
 */
class LruCache<T>(initCap: Int = 10) {
    private val maxSize: Int = initCap
    var count = 0
    private var head = TreeNode<T>(key = "head", value = null, pre = null, next = null)
    private var tail = TreeNode<T>(key = "tail", value = null, pre = null, next = null)
    private val map: HashMap<String, TreeNode<T>> = HashMap();

    init {
        head.next = tail
        tail.pre = head
    }

    fun push(key: String, value: T) {
        val node = map[key]
        if (node != null) {
            moveToTop(node)
            node.value = value
            printTreeNodes()
            return
        }
        val newNode = TreeNode<T>(value, key, null, null)
        moveToTop(newNode)
        map[key] = newNode
        count++
        if (count > maxSize) {
            removeLast()
        }
        printTreeNodes()
    }

    fun get(key: String): T? {
        val node = map[key]
        if (node != null) {
            val next = node.next
            val pre = node.pre
            pre?.next = next
            next?.pre = pre
            moveToTop(node)
            printTreeNodes()
            return node.value
        }
        return null
    }

    private fun moveToTop(treeNode: TreeNode<T>) {
        val next = head.next
        head.next = treeNode
        next?.pre = treeNode
        treeNode.pre = head
        treeNode.next = next
    }

    private fun removeLast() {
        val last = tail.pre
        map.remove(last?.key)
        val pre = last?.pre
        pre?.next = tail
        tail.pre = pre
    }

    fun release() {

    }

    private fun printTreeNodes() {
        print("=====================curNodeList=======================\n")
        var node: TreeNode<T>? = head
        while (node != null) {
            print("<${node.key}>-(${node.value.toString()})-->")
            node = node.next
        }
        print("\n=====================curNodeList end=======================")
    }
}

data class TreeNode<T>(
    var value: T?,
    var key: String?,
    var pre: TreeNode<T>?,
    var next: TreeNode<T>?,
);