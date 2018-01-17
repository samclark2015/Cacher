import java.io.Serializable

class StoredItem(val remotePath: String, val localPath: String, val expiresAt: Long): Serializable {
    override fun toString(): String {
        return "StoredItem(remotePath='$remotePath', localPath='$localPath', expiresAt=$expiresAt)"
    }
}