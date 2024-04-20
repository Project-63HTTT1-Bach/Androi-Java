import pandas as pd
from mlxtend.preprocessing import TransactionEncoder
from itertools import combinations

class AprioriAlgorithm_AssociationRule:
    def __init__(self, min_supp, min_conf):
        self.min_supp = min_supp
        self.min_conf = min_conf
        self.C = []
        self.L = []
        self.TPB = []
        self.TPB_ = []

    def get_unique_values(self, array):
        unique_values = []
        for row in array:
            for value in row:
                if value not in unique_values:
                    unique_values.append(value)
        return unique_values

    def tp_list(self, C_list):
        List = []
        for i in C_list:
            List.append(list(i))
        return List

    def get_C(self, L, k):
        C = []
        if k == 2:
            C = list(combinations(L, k))
            C = self.tp_list(C)
        else:
            C_test = []
            for i in range(len(L)):
                for j in range(i + 1, len(L)):
                    if L[i][:-1] == L[j][:-1]:
                        item = sorted(list(set(L[i]) | set(L[j])))
                        C_test.append(item)
            count = 0
            for item in C_test:
                combinations_list = list(combinations(item, len(item) - 1))
                combinations_list = self.tp_list(combinations_list)
                for i in combinations_list:
                    if i in L:
                        count += 1
                if count == len(combinations_list):
                    C.append(item)
                count = 0
        return C

    def set_L(self, C, df, k):
        L = []
        for item in C:
            if k == 1:
                Supp_L = df[item].mean()
            else:
                Supp_L = df[item].all(axis=1).mean()

            if Supp_L >= self.min_supp:
                L.append(item)
                if k > 1:
                    self.TPB_.append(item)
        if (L != []):
            self.TPB.append(L)
        return L

    def generate_rules(self, frequent_itemsets):
        rules = []
        for itemset in frequent_itemsets:
            for i in range(1, len(itemset)):
                subsets = list(combinations(itemset, r=i))
                for subset in subsets:
                    antecedent = subset
                    consequent = tuple(set(itemset) - set(subset))
                    confidence = self.support(itemset) / self.support(antecedent)
                    if confidence >= self.min_conf:
                        rules.append((antecedent, consequent, confidence))
        return rules

    def support(self, itemset):
        count = 0
        for transaction in self.transactions:
            if set(itemset).issubset(set(transaction)):
                count += 1
        return count

    def find_rules(self, data):
        self.data = data
        self.transactions = data.values.tolist()
        self.transactions = [[str(item) for item in transaction] for transaction in self.transactions]

        te = TransactionEncoder()
        te_ary = te.fit(self.transactions).transform(self.transactions)
        self.df = pd.DataFrame(te_ary, columns=te.columns_)
        
        self.C = self.get_unique_values(self.transactions)
        self.C.sort()

        k = 1
        while True:
            self.L = self.set_L(self.C, self.df, k)
            if len(self.L) <= k:
                break
            k += 1
            self.C = self.get_C(self.L, k)

        self.LKH = self.generate_rules(self.TPB_)
        
    def get_frequent_itemsets(self):
        return self.TPB

    def get_association_rules(self):
        return self.LKH


data = pd.read_csv('E-commerce Customer Behavior weka.csv')

min_supp = 0.33
min_conf = 0.8

apriori = AprioriAlgorithm_AssociationRule(min_supp, min_conf)
apriori.find_rules(data)

frequent_itemsets = apriori.get_frequent_itemsets()
association_rules = apriori.get_association_rules()

print("=== Frequent Itemsets ===\n")
for i in range(len(frequent_itemsets)):
    print("\nL" + str(i+1) + "\n")
    for item in frequent_itemsets[i]:
        print(str(item) + "\n")
    print("\nSize of set of large itemsets L"+ str(i + 1) + " : "+str(len(frequent_itemsets[i])) + "\n")
print("\n\n")
print("=== Association Rules ===\n")
for rule in association_rules:
        print(str(rule[0]) + " -> " + str(rule[1]) + "   conf: " + str(rule[2]) + "\n")