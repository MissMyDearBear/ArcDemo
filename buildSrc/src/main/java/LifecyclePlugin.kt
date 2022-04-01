import com.android.build.api.instrumentation.*
import com.android.build.api.variant.AndroidComponentsExtension
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.objectweb.asm.ClassVisitor
import org.objectweb.asm.util.TraceClassVisitor
import java.io.File
import java.io.PrintWriter

public abstract class LifecyclePlugin : Plugin<Project> {
    override fun apply(project: Project) {
        val android = project.extensions.getByType(AndroidComponentsExtension::class.java)

        android.onVariants { variant ->
            variant.transformClassesWith(
                LifecycleClassVisitorFactory::class.java,
                InstrumentationScope.ALL
            ) {
                it.writeToStdout.set(true)
            }
            variant.setAsmFramesComputationMode(FramesComputationMode.COPY_FRAMES)
        }
    }

    interface ExampleParams : InstrumentationParameters {
        @get:Input
        val writeToStdout: Property<Boolean>
    }

    abstract class LifecycleClassVisitorFactory : AsmClassVisitorFactory<ExampleParams> {
        override fun createClassVisitor(
            classContext: ClassContext,
            nextClassVisitor: ClassVisitor
        ): ClassVisitor {
            return PrivacyClassNode(nextClassVisitor)
//            return if (parameters.get().writeToStdout.get()) {
//                TraceClassVisitor(nextClassVisitor, PrintWriter(System.out))
//            } else {
//                TraceClassVisitor(nextClassVisitor, PrintWriter(File("trace_out")))
//            }
        }


        override fun isInstrumentable(classData: ClassData): Boolean {
            return !classData.className.contains("R$")
        }

    }
}

