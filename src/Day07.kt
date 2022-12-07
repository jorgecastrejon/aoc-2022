fun main() {

    fun part1(input: List<String>): Long {
        val (_, dirs) = generateTree(root = TreeItem.Dir("/"), input)

        return dirs.filter { it.size <= 100000L }.sumOf(TreeItem.Dir::size)
    }

    fun part2(input: List<String>): Long {
        val (root, dirs) = generateTree(root = TreeItem.Dir("/"), input)
        val toBeDelete = 30000000L - (70000000 - root.size)

        return dirs.filter { it.size >= toBeDelete }.minBy(TreeItem.Dir::size).size
    }

    val input = readInput("Day07")

    println(part1(input))
    println(part2(input))
}

private fun generateTree(root: TreeItem.Dir, input: List<String>): Pair<TreeItem.Dir, List<TreeItem.Dir>> {
    val dirs = mutableListOf<TreeItem.Dir>()

    input.fold(root) { tree, string ->

        when {
            string == "\$ cd /" -> root
            string == "\$ cd .." -> tree.parent!!
            string.startsWith("\$ cd") -> {
                tree.items.filterIsInstance<TreeItem.Dir>()
                    .find { it.name == string.split(" ").last() }!!
            }
            string == "\$ ls" -> tree
            string.startsWith("dir") -> {
                val name = string.split(" ").last()
                val dir = TreeItem.Dir(name = name, parent = tree)
                tree.items.add(dir)
                dirs.add(dir)
                tree
            }
            else -> {
                val size = string.split(" ").first().toLong()
                val name = string.split(" ").last()
                val dir = TreeItem.File(name = name, parent = tree, size = size)
                tree.items.add(dir)
                tree
            }
        }
    }

    return root to dirs
}

private sealed class TreeItem {

    abstract val size: Long

    data class Dir(
        val name: String,
        val items: MutableList<TreeItem> = mutableListOf(),
        val parent: Dir? = null
    ) : TreeItem() {
        override val size: Long get() = items.sumOf { it.size }
    }

    data class File(
        val name: String,
        override val size: Long,
        val parent: Dir? = null
    ) : TreeItem()
}
