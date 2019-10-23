import fr.nextek.blog.jsl.VersionGenerator

def call(version) {
 return new VersionGenerator(this).nextVersion(version)
}
