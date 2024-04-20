package org.kettingpowered.task


import org.gradle.api.DefaultTask
import org.gradle.api.file.RegularFileProperty
import org.gradle.api.provider.Property
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction

abstract class GenerateLibs extends DefaultTask {
    @Input abstract Property<String> getConfiguration()
    
    @OutputFile abstract RegularFileProperty getOutput()

    GenerateLibs() {
        outputs.upToDateWhen { false }
        outputs.cacheIf { false }
    }

    @TaskAction
    void genActions() {
        
        def entries = new ArrayList<GString>()
        getProject().configurations.getByName(configuration.get()).resolvedConfiguration.resolvedArtifacts.each { dep->
            def art = dep.moduleVersion.id
            def mavenId = "$art.group:$art.name:$art.version" + (dep.classifier != null ? ":$dep.classifier" : "") + (dep.extension != null ? "@$dep.extension" : "")
            entries.push("$dep.file.sha512\tSHA3-512\t$mavenId")
        }
        output.get().asFile.text = entries.join('\n')
    }
}
