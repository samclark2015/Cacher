import org.junit.After
import org.junit.Test
import java.io.File
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class Tests {
    private val storeFile = "/Users/sam/Desktop/CacheStoreTests"
    private val store = ItemsStore(storeFile)
    @Test
    fun testFetch() {
        val path = "https://www.blog.google/static/blog/images/google-200x200.7714256da16f.png"
        val file = store.fetch(path)
        assertTrue(store.items.containsKey(path))
        assertTrue(file.exists())
    }

    @Test
    fun testRemove() {
        val path = "https://www.blog.google/static/blog/images/google-200x200.7714256da16f.png"
        val file = store.fetch(path)
        store.remove(path)
        assertFalse(store.items.containsKey(path))
        assertFalse(file.exists())
    }

    @After
    fun deleteStore() {
        File(storeFile).deleteRecursively()
    }
}
