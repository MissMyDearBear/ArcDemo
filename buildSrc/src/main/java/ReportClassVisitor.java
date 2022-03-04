import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

public class ReportClassVisitor extends ClassVisitor {

    private String mClassName;

    public ReportClassVisitor(ClassWriter classVisitor) {
        super(Opcodes.ASM7, classVisitor);
    }

    @Override
    public void visit(int version, int access, String name, String signature, String superName, String[] interfaces) {
        System.out.println("ReportClassVisitor: visit-------->started:" + name);
        mClassName = name;
        super.visit(version, access, name, signature, superName, interfaces);
    }

    @Override
    public MethodVisitor visitMethod(int access, String name, String descriptor, String signature, String[] exceptions) {
        System.out.println("ReportClassVisitor: visitMethod-------->methodName:" + name);
        MethodVisitor mv = cv.visitMethod(access, name, descriptor, signature, exceptions);
//        if ("androidx/fragment/app/FragmentActivity.class".equals(this.mClassName)) {
        if ("onCreate".equals(name)) {
            //处理onCreate
            System.out.println("ReportClassVisitor : change method ----> " + name);
            return new LifecycleOnCreateMethodVisitor(mv);
        } else if ("onDestroy".equals(name)) {
            System.out.println("ReportClassVisitor : change method ----> " + name);
            return new LifecycleOnDestroyMethodVisitor(mv);
        }
//        }
        return mv;
    }

    @Override
    public void visitEnd() {
        System.out.println("ReportClassVisitor : visit -----> end");
        super.visitEnd();
    }
}
