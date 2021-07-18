package com.sensorsdata.analytics.android.plugin;

import com.android.build.api.transform.Context
import com.android.build.api.transform.DirectoryInput
import com.android.build.api.transform.Format
import com.android.build.api.transform.JarInput
import com.android.build.api.transform.QualifiedContent
import com.android.build.api.transform.Transform
import com.android.build.api.transform.TransformException
import com.android.build.api.transform.TransformInput
import com.android.build.api.transform.TransformOutputProvider
import com.android.build.gradle.internal.pipeline.TransformManager
import com.android.utils.FileUtils
import org.apache.commons.codec.digest.DigestUtils
import org.gradle.api.Project

class SensorsAnalyticsTransform extends Transform{
    private static Project project

    public SensorsAnalyticsTransform(Project project){
        this.project = project
    }

    //这个是任务Task的名称
    @Override
    String getName() {
        return "SensorsAnalyticsAutoTrack"
    }

    /**
     * 需要处理的数据类型，有两种枚举类型
     *TransformManager.CONTENT_CLASS  代表处理java的class文件
     * TransformManager.CONTENT_RESOURCES 代表java的资源文件
     * @return
     */
    @Override
    Set<QualifiedContent.ContentType> getInputTypes() {


        return  TransformManager.CONTENT_CLASS
    }

    /**
     *
     *  PROJECT 只处理当前项目
     *  SUB——POROJECT 只处理子项目
     *  PROJECT-LOCAL-DEPS  只处理项目中的本地依赖 例如 jar，aar
     *  SUB_PROJECTS_LOCAL_DECPS 只处理子项目的本地依赖
     *  EXTERNAL_LIBRARIES 只处理外部项目
     *  PROVIDED_ONLY 只处理本地或以provided形式引入的依赖库
     *  TESTED_CODE 测试代码
     *  TransformManager.SCOPE_FULL_PROJECT
     * @return
     */
    @Override
    Set<? super QualifiedContent.Scope> getScopes() {
        return TransformManager.SCOPE_FULL_PROJECT
    }

    //是否增量构建
    @Override
    boolean isIncremental() {
        return false
    }

    static void printCopyRight(){
        println()
        println("#########################################")
        println("#########                     ###########")
        println("#########                     ###########")
        println("#########       AZY           ###########")
        println("#########                     ###########")
        println("#########                     ###########")
        println("#########################################")
        println()
    }

    @Override
    void transform(Context context, Collection<TransformInput> inputs, Collection<TransformInput> referencedInputs, TransformOutputProvider outputProvider, boolean isIncremental) throws IOException, TransformException, InterruptedException {
       printCopyRight()
        //Transform的Inputs有两种类型，一种是目录，一种是jar包，要分开遍历
        inputs.each {TransformInput input ->
            //遍历目录
            input.directoryInputs.each { DirectoryInput directoryInput ->
                //获取output目录
                def dest = outputProvider.getContentLocation(directoryInput.name,directoryInput.contentTypes,directoryInput.scopes,
                        Format.DIRECTORY)
                //将input目录复制到output目录
                FileUtils.copyDirectory(directoryInput.file,dest)
            }
            //遍历jar
            input.jarInputs.each { JarInput jarInput ->
                //重命名输出文件
                def jarName = jarInput.name
                def md5Name = DigestUtils.md5Hex(jarInput.file.getAbsolutePath())
                if(jarName.endsWith(".jar")){
                    jarName = jarName.substring(0,jarName.length() - 4)
                }
                File copyJarFile = jarInput.file
                //生成输出路径
                def dest = outputProvider.getContentLocation(jarName+md5Name,
                        jarInput.contentTypes,jarInput.scopes,Format.JAR)
                //将input的目录复制到output指定目录
                FileUtils.copyFile(copyJarFile,dest)
            }
        }


    }
}