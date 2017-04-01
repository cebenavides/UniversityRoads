# -*- coding: utf-8 -*-
"""
Created on Fri Mar 31 18:59:17 2017

"""
# Libraries
import matplotlib.pyplot as plt
import networkx as nx
import numpy as np

def drawing_graph(g,path):
    '''
    This function draws the graph g in circular layout, labeling the nodes
    and the edges.
    '''
    # Defining a circular layout for printing the graph
    pos=nx.circular_layout(g)
     
    # Drawing the graph with nodes labels
    nx.draw_networkx_nodes(g,pos,node_size=700)
    nx.draw_networkx_edges(g,pos,width=6)
    nx.draw_networkx_labels(uni,pos,font_size=20,font_family='sans-serif')
    
    # Labeling the edges with their weights
    #edge_labels = dict([((u,v,),d['weight']) for u,v,d in g.edges(data=True)])
    #nx.draw_networkx_edge_labels(g,pos,edge_labels=edge_labels)
    
    # Highlighting the nodes and edges from the selected path
    path_edges = zip(path,path[1:])
    nx.draw_networkx_nodes(g,pos,nodelist=path,node_color='g',node_size=700)
    nx.draw_networkx_edges(g,pos,edgelist=path_edges,edge_color='b',width=10)    
    
    plt.axis('off')
    plt.show()


if __name__ == '__main__':
    # Reading the graph from the multiline adjacency list
    uni = nx.read_multiline_adjlist("test.adjlist")    
    
    # Reading the source and destination nodes (missing validation)
    source_node = raw_input('Enter the name of the source node: ')
    dest_node = raw_input('Enter the name of the destination node: ')
    
    # Computing the shortest paths between all the nodes
    paths = nx.shortest_path(uni,weight='weight')
    paths_lengths = nx.shortest_path_length(uni,weight='weight')
    
    print '\n***** RESULTS *****'
    print 'Shortest path:',paths[source_node][dest_node]
    print 'Lenght:',paths_lengths[source_node][dest_node]
    drawing_graph(uni,paths[source_node][dest_node])