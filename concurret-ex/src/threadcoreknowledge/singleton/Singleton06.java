package threadcoreknowledge.singleton;

/**
 * singleton03:  thread-unsafe, double check
 */
public class Singleton06 {

    private static Singleton06 INSTANCE;

    private Singleton06() {

    }

    public static Singleton06 getInstance() {
        if (INSTANCE == null) {
            synchronized (Singleton06.class) {
                if (INSTANCE == null) {
                    INSTANCE = new Singleton06();
                }

            }

        }
        return INSTANCE;
    }

    /**
     * public static getInstance()Lthreadcoreknowledge/singleton/Singleton06;
     * TRYCATCHBLOCK L0 L1 L2 null
     * TRYCATCHBLOCK L2 L3 L2 null
     * L4
     * LINENUMBER 15 L4
     * GETSTATIC threadcoreknowledge/singleton/Singleton06.INSTANCE : Lthreadcoreknowledge/singleton/Singleton06;
     * IFNONNULL L5
     * L6
     * LINENUMBER 16 L6
     * LDC Lthreadcoreknowledge/singleton/Singleton06;.class
     * DUP
     * ASTORE 0
     * MONITORENTER
     * L0
     * LINENUMBER 17 L0
     * GETSTATIC threadcoreknowledge/singleton/Singleton06.INSTANCE : Lthreadcoreknowledge/singleton/Singleton06;
     * IFNONNULL L7
     * L8
     * LINENUMBER 18 L8
     * NEW threadcoreknowledge/singleton/Singleton06
     * DUP
     * INVOKESPECIAL threadcoreknowledge/singleton/Singleton06.<init> ()V
     * PUTSTATIC threadcoreknowledge/singleton/Singleton06.INSTANCE : Lthreadcoreknowledge/singleton/Singleton06;
     * L7
     * LINENUMBER 21 L7
     * FRAME APPEND [java/lang/Object]
     * ALOAD 0
     * MONITOREXIT
     * L1
     * GOTO L5
     * L2
     * FRAME SAME1 java/lang/Throwable
     * ASTORE 1
     * ALOAD 0
     * MONITOREXIT
     * L3
     * ALOAD 1
     * ATHROW
     * L5
     * LINENUMBER 24 L5
     * FRAME CHOP 1
     * GETSTATIC threadcoreknowledge/singleton/Singleton06.INSTANCE : Lthreadcoreknowledge/singleton/Singleton06;
     * ARETURN
     * MAXSTACK = 2
     * MAXLOCALS = 2
     * <p>
     * // access flags 0x9
     * public static main([Ljava/lang/String;)V
     * L0
     * LINENUMBER 28 L0
     * POP
     * L1
     * LINENUMBER 29 L1
     * RETURN
     * L2
     * LOCALVARIABLE args [Ljava/lang/String; L0 L2 0
     * MAXSTACK = 1
     * MAXLOCALS = 1
     * }
     */
    public static void main(String[] args) {
        getInstance();
    }

}
