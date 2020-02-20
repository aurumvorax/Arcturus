package aurumvorax.arcturus.aiUtree;

public abstract class Node<Blackboard>{

    protected abstract float evaluate(Blackboard bb);
    protected abstract void execute(Blackboard bb);
}
