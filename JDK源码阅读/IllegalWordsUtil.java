
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * 过滤敏感词
 * @author wang xiao
 * @date Created in 14:10 2020/12/9
 */
public class IllegalWordsUtil {


   /**
    *   字典树
    * @author wangxiao
    */
    private class Node {
        /**
         * 是否为叶子节点
         */
        public boolean isWord;
       /**
        * 子节点
        */
        public HashMap<Character, Node> children;

        public Node(boolean isWord) {
            this.isWord = isWord;
            this.children = new HashMap<>();
        }

        public Node() {
            this(false);
        }
    }

    /**
     * trie树的根节点
     */
    private final Node root;
    /**
     * size
     */
    private int size;

    private final List<Character> skipWord = new ArrayList<>(Arrays.asList('?','!','|',',','*','/','@','#','$','%','(',')'));

    private final List<String> prefixList = new ArrayList<String>();

    private IllegalWordsUtil() {
        this.root = new Node();
        this.size = 0;
    }


    public int getSize() {
        return size;
    }

    /**
     *  添加敏感词
     * @author wangxiao
     * @date 14:21 2020/12/9
     * @param word word
     */
    public void addBranchesInTrie(String word) {
        Node cur = root;
        char[] words = word.toCharArray();
        for (char c : words) {
            if (!cur.children.containsKey(c)) {
                cur.children.put(c, new Node());
            }
            cur = cur.children.get(c);
        }
        if (!cur.isWord) {
            cur.isWord = true;
            size++;
        }
    }

    /**
     *  判断trie树中是否存在某分枝/敏感词
     * @author wangxiao
     * @date 14:29 2020/12/9
     * @param word word
     * @return boolean
     */
    public boolean contains(String word) {
        Node cur = root;
        char[] words = word.toCharArray();
        for (char c : words) {
            if (skipWord.contains(c)){
                continue;
            }
            if (!cur.children.containsKey(c)) {
                return false;
            }
            cur = cur.children.get(c);
        }
        return cur.isWord;
    }

    /**
     *  如果一段话中有trie树中存储的敏感词则需将其进行替换为 **; 例如：尼玛的，替换为 **的
     * @author wangxiao
     * @date 14:29 2020/12/9
     * @param word word
     * @return java.lang.String
     */
    public String doSensitive2Replace(String word) {
        Node cur = root;
        char[] words = word.toCharArray();
        // old && start
        StringBuilder oldTemp = new StringBuilder();
        StringBuilder starTemp = new StringBuilder();
        for (char c : words) {
            if (!cur.children.containsKey(c)) {
                if (Character.isWhitespace(c)  && oldTemp.length() > 0 ) {
                    oldTemp.append(c);
                    starTemp.append("*");
                }
                continue;
            }
            if (!cur.isWord) {
                oldTemp.append(c);
                starTemp.append("*");
                cur = cur.children.get(c);
            }
            if (cur.isWord) {
                word = word.replaceAll(oldTemp.toString(), starTemp.toString());
                oldTemp.delete(0, oldTemp.length());
                starTemp.delete(0, starTemp.length());
                cur = root;
            }
        }
        return word;
    }



    /**
     *  利用trie的公共前缀特性，可以实现关键词自动联想
     * @author wangxiao
     * @date 14:32 2020/12/9
     * @param word word
     * @param root root
     */
    public void prefixMatching(String word, Node root) {
        Node cur = root;
        char[] words = word.toCharArray();
        StringBuilder str = new StringBuilder();
        str.append(word);
        for (int i = 0; i < words.length; i++) {
            if (!cur.children.containsKey(words[i])) {
                return;
            }
            cur = cur.children.get(words[i]);
        }
        dfs(str, cur);
    }

   /**
    *  节点遍历
    * @author wangxiao
    * @date 14:32 2020/12/9
    * @param word word
    * @param root root
    */
    public void dfs(StringBuilder word, Node root) {
        Node cur = root;
        if (cur.isWord) {
            prefixList.add(word.toString());
            if (cur.children.size() == 0) {
                return;
            }
        }
        for (Character s : cur.children.keySet()) {
            word.append(s);
            // 递归调用
            dfs(word, cur.children.get(s));
            word.delete(word.length() - 1, word.length());
        }
    }

    // test
    public static void main(String[] args) {
        IllegalWordsUtil t = new IllegalWordsUtil();
        // 插入敏感词
        t.addBranchesInTrie("麻痹");
        t.addBranchesInTrie("尼玛的");
        t.addBranchesInTrie("狗日的");

        // 插入联想词
        t.addBranchesInTrie("联想云科技");
        t.addBranchesInTrie("联盟");
        t.addBranchesInTrie("联和利泰扩招了");

        System.out.println("trie树中分枝的个数：" + t.size);

        String word = "尼玛";
        System.out.println("Trie树中是否存在[ " + word + " ]敏感词: " + t.contains(word));
        // 敏感词替换测试
        t.doSensitive2Replace("衮，尼 玛 的 傻子，你麻痹的，你各狗日的，早晚揍死你。");

        // trie树实现联想测试
        t.prefixMatching("联", t.root);

    }


}
