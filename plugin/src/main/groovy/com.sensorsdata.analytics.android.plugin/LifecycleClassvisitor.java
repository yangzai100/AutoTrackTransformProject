package com.sensorsdata.analytics.android.plugin;

import org.objectweb.asm.ClassVisitor;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * create by  zhaoyang ao  2021/8/29
 */
class LifecycleClassvisitor extends ClassVisitor {
    String className;
    String superName;


    public LifecycleClassvisitor( ClassVisitor classVisitor) {
        super(Opcodes.ASM5, classVisitor);
    }

    @Override
    public void visit(int version, int accss, String nam, String signature, String superName, String[] exceptions) {
        super.visit(version, accss, nam, signature, superName, exceptions);
        className = nam;
        this.superName = superName;
    }

    @Override
    public MethodVisitor visitMethod(int i, String name, String s1, String s2, String[] strings) {
        System.out.println("classVisitor visitMethod name ---" + name + "super name---" + superName);
        MethodVisitor methodVisitor = cv.visitMethod(i, name, s1, s2, strings);
        if(superName.equals("androidx/appcompat/app/AppCompatActivity")){
            System.out.println("拦截到Acivity");
            if (name.equals("onCreate")) {
                System.out.println("拦截到onCreate");
                return new LifecycleMethodVisitor(methodVisitor,className,name);
            }
        }
        return methodVisitor;

    }
}
