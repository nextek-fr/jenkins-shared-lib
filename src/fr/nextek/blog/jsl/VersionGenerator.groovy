package fr.nextek.blog.jsl

class VersionGenerator implements Serializable {

    def script

    VersionGenerator(script) {
        this.script = script
    }

    def nextVersion(version) {
        def tags = retrieveGitTagsForVersion(version)
        def tagsArray = gitTags.split('\n')
        return  newVersion(tagsArray, version)
    }

    def retrieveGitTagsForVersion(version) {
        return script.sh(returnStdout: true, script: "git tag -l \"v${version}.*\"")
    }

    def newVersion(def listOfExistingVersions, String currentVersion) {
        assert listOfExistingVersions: "We need listOfExistingVersions to be valid"
        assert currentVersion: "We need currentVersion to be valid"
        script.echo "list of existing versions : $listOfExistingVersions"
        List<Integer> filteredVersions = new ArrayList<Integer>();
        int versionDigits = currentVersion.split('\\.').size()
        for (int i =0; i < listOfExistingVersions.size(); i++) {
            String raw = listOfExistingVersions[i]
            def versionElements = raw.split('\\.')
            if (versionElements.size() > versionDigits) {
                def patchRaw = versionElements[versionDigits]
                if (patchRaw.matches('\\d')) {
                    filteredVersions.add(new Integer(patchRaw))
                }
            }
        }
        Collections.sort(filteredVersions)
        int patchPrevious = 0
        int patchNext = patchPrevious
        if (filteredVersions.isEmpty()) {
            script.echo "We found no existing tag, so version will be .0"
        } else {
            patchPrevious = filteredVersions.get(filteredVersions.size() - 1)
            patchNext = patchPrevious + 1
        }
        return currentVersion + "." + patchNext
    }

}
