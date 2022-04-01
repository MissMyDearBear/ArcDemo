import org.objectweb.asm.tree.ClassNode
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.Opcodes


class PrivacyClassNode(private val nextVisit: ClassVisitor) : ClassNode(Opcodes.ASM5) {

    override fun visitEnd() {
        super.visitEnd()
        println("===================bear-->visit<$name>")
        accept(nextVisit)
    }

}