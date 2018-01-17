import org.apache.commons.io.FileUtils
import java.io.*
import java.net.URL
import java.time.Duration
import java.util.*
import kotlin.collections.HashMap

class ItemsStore(val cacheLocation: String, val cacheTime: Duration = Duration.ofHours(2)) {
    private val cacheFile = File(cacheLocation, "data.bin")
    val items = HashMap<String, StoredItem>()

    init {
        if(cacheFile.exists()) {
            val fis = FileInputStream(cacheFile)
            val ois = ObjectInputStream(fis)
            val i = ois.readObject()
            if(i is HashMap<*, *>) {
                items.putAll(i as HashMap<String, StoredItem>)
            }
        }
    }

    fun fetch(path: String): File {
        val retrievedItem = items[path]
        return if(retrievedItem != null) {
            // If item is cached, return local copy
            File(retrievedItem.localPath)
        } else {
            // Create cache file copy
            val url = URL(path)
            val file = File(cacheLocation, UUID.randomUUID().toString())
            FileUtils.copyURLToFile(url, file)
            // Create item in hash map store
            val item = StoredItem(url.toExternalForm(), file.absolutePath, 0) // TODO(Implement expiration)
            items[path] = item
            saveCacheData()

            File(item.localPath)
        }
    }

    fun remove(path: String) {
        val retrievedItem = items[path]
        if(retrievedItem != null) {
            val file = File(retrievedItem.localPath)
            file.delete()
            items.remove(path)
            saveCacheData()
        }
    }

    fun empty() {
        items.forEach { k, v ->
            File(v.localPath).delete()
        }
        items.clear()
        saveCacheData()
    }

    private fun saveCacheData() {
        // Save cache binary data
        val fos = FileOutputStream(cacheFile)
        val oos = ObjectOutputStream(fos)
        oos.writeObject(items)
    }
}