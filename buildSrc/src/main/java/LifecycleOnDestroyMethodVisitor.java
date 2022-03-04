import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;


public class LifecycleOnDestroyMethodVisitor extends MethodVisitor {

    public LifecycleOnDestroyMethodVisitor(MethodVisitor mv) {
        super(Opcodes.ASM7, mv);
    }

    @Override
    public void visitCode() {
        super.visitCode();
        mv.visitLdcInsn("bear-->");
        mv.visitLdcInsn("<onDestroy>");
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "e", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }


    @Override
    public void visitInsn(int opcode) {
        super.visitInsn(opcode);
    }

}
