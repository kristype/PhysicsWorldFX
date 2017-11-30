package framework;

/**
 * Full: The node is fully simulated and will interact with the world and other nodes
 * NonMovable: Gives the node infinite mass witch results in that the node cannot be moved,
 *             but will interact with nodes that are fully simulated
 * Movable: Gives the node infinite mass, but can be moved and will interact with fully simulated nodes.
 *
 * NonMovable and Movable nodes cannot interact with other NonMovable or Movable nodes because they have infinite mass,
 * and it is therefore impossible to calculate interaction.
 */
public enum SimulationType {
    Full,
    Movable,
    NonMovable
}
