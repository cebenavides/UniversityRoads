# -*- coding: utf-8 -*-
"""
Created on Fri Mar 31 19:44:16 2017

"""
import networkx as nx

if __name__ == '__main__':
    uni = nx.Graph()
    uni.add_edge('A','B',weight=0.6)
    uni.add_edge('A','C',weight=15)
    uni.add_edge('C','D',weight=2)
    uni.add_edge('C','E',weight=0.7)
    uni.add_edge('C','F',weight=0.9)
    uni.add_edge('A','D',weight=3)
    nx.write_multiline_adjlist(uni,"test.adjlist")