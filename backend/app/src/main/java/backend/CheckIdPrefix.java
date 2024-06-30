package backend;
public class CheckIdPrefix {
    public static String checkAndAddPrefix(String id, String prefix) {
        // Check if the ID starts with the given prefix (case-insensitive)
        if (id.toLowerCase().startsWith(prefix.toLowerCase())) {
            return id;
        } else {
            return prefix + id;
        }
    }
}
