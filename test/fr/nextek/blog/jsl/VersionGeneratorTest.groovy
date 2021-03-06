package fr.nextek.blog.jsl

import org.jenkinsci.plugins.workflow.cps.CpsScript;

class VersionGeneratorTest extends GroovyTestCase {

    def mockCpsScript() {
        return [
                'sh'    : { arg ->
		    def mask=((Map)arg[0]).get('script').split('\\s+')[3].toString()
                    if (mask.equals("\"v1.3.*\""))
                        return "v1.3.0\nv1.3.1\nv1.3.2"
		    return ""
                },
                'echo'  : { arg ->
                    println(arg[0])
                }
        ] as CpsScript
    }

    void testNextVersionOfV1_3() {
	nextVersion('1.3', '1.3.3')
    }

    void testNextVersionOfV1_4() {
	nextVersion('1.4', '1.4.0')
    }

    void nextVersion(String currentVersion, String expectedVersion) {
	assert expectedVersion == new VersionGenerator(mockCpsScript()).nextVersion(currentVersion)
    }

}
