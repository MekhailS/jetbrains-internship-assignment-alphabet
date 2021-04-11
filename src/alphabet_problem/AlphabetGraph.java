package alphabet_problem;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;

import java.util.ArrayList;
import java.util.Stack;

/**
 * Represent alphabet as a graph: characters as nodes, edges — order relation between characters
 * (a->b <=> a>b)
 */
public class AlphabetGraph
{
    /**
     * constructor of 'basic' alphabet graph:
     * no edges between nodes
     */
    public AlphabetGraph()
    {
        for (int i = 0; i < graph.length; i++)
        {
            graph[i] = new GraphNode();
        }
    }

    /**
     * add edges between nodes based on order of stings in sorted array
     *
     * @param strArr array of sorted strings in descending order
     */
    public void constructEdgesBySortedStringArr(@NotNull String[] strArr)
    {
        for (int i = 1; i < strArr.length; i++)
        {
            insertEdgeByStringPair(strArr[i-1], strArr[i]);
        }
    }

    /**
     * <pre>
     *      insert edge in graph between two character nodes:
     *
     *      example:
     *      strGreater := 'aaa'
     *      strLess := 'aba'
     *      => insert edge a -> b
     * </pre>
     * @param strGreater string which is greater
     * @param strLess string less than string above
     */
    public void insertEdgeByStringPair(@NotNull String strGreater, @NotNull String strLess)
    {
        int idxChar = 0;
        while (strGreater.charAt(idxChar) == strLess.charAt(idxChar))
        {
            idxChar++;
            if (idxChar >= strGreater.length() || idxChar >= strLess.length())
                return;
        }
        insertEdge(mapCharToVertexIdx(strGreater.charAt(idxChar)), mapCharToVertexIdx(strLess.charAt(idxChar)));
    }

    /**
     * get string of character values of nodes topologically sorted (descending order)
     * @return string which represents nodes (as characters) sorted topologically in descending order
     */
    @Nullable
    public String getTopologicallySortedAlphabet()
    {
        ArrayList<Integer> sortedNodes = getTopologicallySortedNodes();
        if (sortedNodes == null)
        {
            return null;
        }

        StringBuilder stringBuilder = new StringBuilder(sortedNodes.size());
        for (Integer sortedNode : sortedNodes)
        {
            stringBuilder.append(mapVertexIdxToChar(sortedNode));
        }
        stringBuilder.reverse();
        return stringBuilder.toString();
    }

    /**
     * instances of this class represent status (color) of graph nodes in DFS traverse
     */
    static private class DataDFS
    {
        enum Color
        {
            WHITE, // node wasn't visited
            GREY, // node is in 'process of visiting' (traversing through all paths from this node)
            BLACK // node was 'fully visited': all paths from this node were traversed
        }
        Color clr = Color.WHITE;
    }

    /**
     * instances of this class used as elements of DFS traverse stack
     */
    static private class DFSStackElem
    {
        DFSStackElem(int nodeIdx, boolean visitedFromHimself)
        {
            this.nodeIdx = nodeIdx;
            this.visitedFromHimself = visitedFromHimself;
        }

        int nodeIdx;

        /*
         flag used to understand when we have traversed all paths from this node
         and returned to this node again
         */
        boolean visitedFromHimself;
    }

    /**
     * <pre>
     *      get topologically sorted indices of graph nodes (a -> b <=> a > b)
     *
     *      topological sort implemented with DFS traverse
     *      (iterative, not recursive)
     * </pre>
     *
     * @return array of topologically sorted indices of graph's nodes (ascending order),
     * null if graph couldn't be sorted (exists cycle path in graph)
     */
    @Nullable
    private ArrayList<Integer> getTopologicallySortedNodes()
    {
        ArrayList<Integer> idxTopologicallySorted = new ArrayList<>();
        DataDFS[] nodesDFSData = new DataDFS[graph.length];
        for (int i = 0; i < nodesDFSData.length; i++)
        {
            nodesDFSData[i] = new DataDFS();
        }

        Stack<DFSStackElem> dfsStack = new Stack<>();
        for (int i = 0; i < graph.length; i++)
        {
            dfsStack.push(new DFSStackElem(i, false));
        }

        while (!dfsStack.empty())
        {
            DFSStackElem curNode = dfsStack.pop();

            DataDFS.Color clr = nodesDFSData[curNode.nodeIdx].clr;
            ArrayList<Integer> adjNodes = nodeByIdx(curNode.nodeIdx).adjNodesIdx;

            if (curNode.visitedFromHimself)
            {
                /*
                 mark node as 'fully visited' (all adjacent nodes were visited)
                 add to result idx array
                 */
                nodesDFSData[curNode.nodeIdx].clr = DataDFS.Color.BLACK;
                idxTopologicallySorted.add(curNode.nodeIdx);
                continue;
            }

            if (clr == DataDFS.Color.GREY)
            {
                // found cycle: path from this node to itself
                return null;
            }
            else if (clr == DataDFS.Color.WHITE)
            {
                /*
                 since we want to return to current node after visiting all adjacent ones,
                 we will push it to stack with flag 'visitedFromHimself = true'
                 */
                dfsStack.push(new DFSStackElem(curNode.nodeIdx, true));

                nodesDFSData[curNode.nodeIdx].clr = DataDFS.Color.GREY;
                for (Integer adjNode : adjNodes)
                {
                    dfsStack.push(new DFSStackElem(adjNode, false));
                }
            }
        }

        return idxTopologicallySorted;
    }

    private void insertEdge(int nodeStartIdx, int nodeEndIdx)
    {
        nodeByIdx(nodeStartIdx).adjNodesIdx.add(nodeEndIdx);
    }

    private static int mapCharToVertexIdx(char c)
    {
        return (c - 'a');
    }

    private static char mapVertexIdxToChar(int vertexIdx)
    {
        return (char)('a' + vertexIdx);
    }

    private GraphNode nodeByIdx(int idx)
    {
        return graph[idx];
    }

    /*
     since we know that there are 26 letters in alphabet (from 'a' to 'z') we can store alphabet graph
     as an adjacency list, accessing the node with character "c" by index "c-'a'" in graph
     (this way we actually get custom implementation of simple HashMap (HashTable))

     probably more readable and easy to understand solution is to use
     HashMap<char, GraphNode>
     */
    private GraphNode[] graph = new GraphNode[('z' - 'a') + 1];

    static private class GraphNode
    {
        ArrayList<Integer> adjNodesIdx = new ArrayList<>();
    }
}




