package com.sensorsdata.analytics.android.plugin;

import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * create by  zhaoyang ao  2021/8/29
 */
class LifecycleMethodVisitor extends MethodVisitor {
    String className;
    String superName;

    public LifecycleMethodVisitor( MethodVisitor methodVisitor,String className,String superName) {
        super(Opcodes.ASM5, methodVisitor);
        this.className = className;
        this.superName = superName;
    }

    //在方法执行前插入
    @Override
    public void visitCode() {
        super.visitCode();
        System.out.println("Method Visitor visitCode ---");
        ///这里的代码可以使用ASMBByteCode Outline 生产
        mv.visitLdcInsn("TAG");
        mv.visitLdcInsn(className + " ====== "+superName);
        mv.visitMethodInsn(Opcodes.INVOKESTATIC, "android/util/Log", "i", "(Ljava/lang/String;Ljava/lang/String;)I", false);
        mv.visitInsn(Opcodes.POP);
    }
}
