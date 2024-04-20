import numpy as np
import pandas as pd
import math
from sklearn.model_selection import train_test_split
from sklearn.metrics import classification_report, confusion_matrix, accuracy_score

# Định nghĩa lớp TreeNode để biểu diễn một nút trong cây quyết định
class TreeNode:
    def __init__(self, data, output):
        self.data = data  # Dữ liệu của nút, thường là chỉ số của thuộc tính
        self.children = {}  # Các nút con của nút hiện tại
        self.output = output  # Đầu ra của nút (chỉ được sử dụng nếu nút là lá của cây)
        self.index = -1  # Chỉ số của nút trong tập dữ liệu

    # Phương thức để thêm nút con vào nút hiện tại
    def add_child(self, feature_value, obj):
        self.children[feature_value] = obj

# Định nghĩa lớp DecisionTreeClassifier để xây dựng và huấn luyện mô hình cây quyết định
class DecisionTreeClassifier:
    def __init__(self):
        self.__root = None  # Nút gốc của cây

    # Đếm số lượng các phần tử duy nhất trong một danh sách
    def count_unique(self, Y):
        d = {}  # Tạo một từ điển để lưu trữ số lần xuất hiện của mỗi phần tử trong danh sách Y
        for i in Y:  # Duyệt qua từng phần tử trong danh sách Y
            if i not in d:  # Nếu phần tử chưa có trong từ điển d
                d[i] = 1  # Thêm phần tử vào từ điển với giá trị là 1 (đã xuất hiện lần đầu tiên)
            else:  # Nếu phần tử đã tồn tại trong từ điển
                d[i] += 1  # Tăng giá trị của phần tử trong từ điển lên 1 (đã xuất hiện thêm một lần)
        return d  # Trả về từ điển d với số lần xuất hiện của mỗi phần tử trong danh sách Y


    # Tính entropy của một tập dữ liệu
    def entropy(self, Y):
        freq_map = self.count_unique(Y)  # Đếm số lần xuất hiện của mỗi phần tử trong danh sách Y và lưu vào từ điển freq_map
        entropy = 0  # Khởi tạo entropy ban đầu bằng 0
        total = len(Y)  # Đếm tổng số lượng mẫu trong tập dữ liệu Y
        for i in freq_map:  # Duyệt qua các phần tử trong từ điển freq_map
            p = freq_map[i] / total  # Tính tỷ lệ của số lần xuất hiện của phần tử i trong tổng số mẫu
            entropy += (-p) * math.log2(p)  # Tính entropy cho phần tử i và cộng vào entropy tổng
        return entropy  # Trả về giá trị entropy của tập dữ liệu Y


    # Tính độ lợi thông tin khi chia tập dữ liệu theo một thuộc tính
    def information_gain(self, X, Y, selected_feature):
        info_orig = self.entropy(Y)  # Tính entropy ban đầu của tập dữ liệu Y
        info_f = 0  # Khởi tạo giá trị entropy sau khi chia (tính toán từng giá trị của thuộc tính được chọn)
        values = set(X[:, selected_feature])  # Lấy tập hợp các giá trị duy nhất của thuộc tính được chọn từ tập dữ liệu X
        df = pd.DataFrame(X)  # Tạo DataFrame từ tập dữ liệu X
        df[df.shape[1]] = Y  # Thêm cột dữ liệu Y vào DataFrame để thực hiện tính toán dễ dàng hơn
        initial_size = df.shape[0]  # Số lượng mẫu ban đầu trong tập dữ liệu
        for i in values:  # Duyệt qua từng giá trị của thuộc tính được chọn
            df1 = df[df[selected_feature] == i]  # Lọc các mẫu có giá trị thuộc tính được chọn là i
            current_size = df1.shape[0]  # Số lượng mẫu sau khi lọc
            info_f += (current_size / initial_size) * self.entropy(df1[df1.shape[1]-1])  # Tính entropy sau khi chia và cộng vào tổng entropy sau khi chia
        info_gain = info_orig - info_f  # Tính độ lợi thông tin bằng hiệu của entropy ban đầu và entropy sau khi chia
        return info_gain  # Trả về độ lợi thông tin

    # Xây dựng cây quyết định bằng đệ quy
    def decision_tree(self, X, Y, features, level, metric, classes):
        # Nếu tất cả các mẫu thuộc cùng một lớp, trả về nút lá với đầu ra là lớp đó
        if len(set(Y)) == 1:  # Kiểm tra xem tất cả các nhãn trong tập dữ liệu Y có cùng một lớp hay không
            output = None  # Khởi tạo đầu ra ban đầu
            for i in classes:  # Duyệt qua các lớp trong tập hợp lớp
                if i in Y:  # Nếu một lớp trong tập hợp lớp tồn tại trong Y
                    output = i  # Gán lớp đó là đầu ra
            return TreeNode(None, output)  # Trả về một nút lá với đầu ra là lớp duy nhất trong tập dữ liệu Y

        # Nếu không còn thuộc tính nào để chia, trả về nút lá với đầu ra là lớp phổ biến nhất
        if len(features) == 0:  # Kiểm tra xem có còn thuộc tính nào để chia không
            freq_map = self.count_unique(Y)  # Đếm số lần xuất hiện của từng lớp trong tập dữ liệu Y
            output = None  # Khởi tạo giá trị đầu ra
            max_count = -math.inf  # Khởi tạo biến để theo dõi số lần xuất hiện lớn nhất
            for i in classes:  # Duyệt qua từng lớp trong tập hợp lớp
                if i in freq_map:  # Nếu lớp tồn tại trong tập dữ liệu
                    if freq_map[i] > max_count:  # Nếu số lần xuất hiện của lớp lớn hơn số lần xuất hiện lớn nhất hiện tại
                        output = i  # Cập nhật giá trị đầu ra là lớp đó
                        max_count = freq_map[i]  # Cập nhật số lần xuất hiện lớn nhất
            return TreeNode(None, output)  # Trả về một nút lá với đầu ra là lớp phổ biến nhất trong tập dữ liệu Y

        # Chọn thuộc tính tối ưu để chia
        max_gain = -math.inf  # Khởi tạo độ lợi thông tin lớn nhất ban đầu là âm vô cùng
        final_feature = None  # Khởi tạo thuộc tính tối ưu là None
        for f in features:  # Duyệt qua từng thuộc tính trong danh sách các thuộc tính còn lại
            curr_gain = self.information_gain(X, Y, f)  # Tính toán độ lợi thông tin của thuộc tính hiện tại
            if curr_gain > max_gain:  # Nếu độ lợi thông tin hiện tại lớn hơn độ lợi thông tin lớn nhất đã tìm thấy
                max_gain = curr_gain  # Cập nhật độ lợi thông tin lớn nhất
                final_feature = f  # Cập nhật thuộc tính tối ưu

        # Tạo nút cho thuộc tính được chọn
        freq_map = self.count_unique(Y)  # Đếm số lần xuất hiện của mỗi lớp trong tập dữ liệu Y
        output = None  # Khởi tạo đầu ra ban đầu
        max_count = -math.inf  # Khởi tạo biến để theo dõi số lần xuất hiện lớn nhất
        for i in classes:  # Duyệt qua từng lớp trong tập hợp lớp
            if i in freq_map:  # Nếu lớp tồn tại trong tập dữ liệu
                if freq_map[i] > max_count:  # Nếu số lần xuất hiện của lớp lớn hơn số lần xuất hiện lớn nhất hiện tại
                    output = i  # Cập nhật giá trị đầu ra là lớp đó
                    max_count = freq_map[i]  # Cập nhật số lần xuất hiện lớn nhất
        unique_values = set(X[:, final_feature])  # Lấy tập hợp các giá trị duy nhất của thuộc tính được chọn từ tập dữ liệu X
        df = pd.DataFrame(X)  # Tạo DataFrame từ tập dữ liệu X
        df[df.shape[1]] = Y  # Thêm cột dữ liệu Y vào DataFrame để thực hiện tính toán dễ dàng hơn
        current_node = TreeNode(final_feature, output)  # Tạo nút mới với thuộc tính được chọn và đầu ra là lớp phổ biến nhất
        index = features.index(final_feature)  # Lấy chỉ mục của thuộc tính được chọn trong danh sách các thuộc tính
        features.remove(final_feature)  # Loại bỏ thuộc tính được chọn khỏi danh sách các thuộc tính

        # Tạo nút con cho mỗi giá trị riêng biệt của thuộc tính được chọn
        for i in unique_values:  # Duyệt qua từng giá trị riêng biệt của thuộc tính được chọn
            df1 = df[df[final_feature] == i]  # Lọc các mẫu có giá trị thuộc tính được chọn là i
            node = self.decision_tree(df1.iloc[:, 0:df1.shape[1]-1].values, df1.iloc[:, df1.shape[1]-1].values, features, level+1, metric, classes)  # Gọi đệ quy để xây dựng cây cho tập dữ liệu con đã lọc
            current_node.add_child(i, node)  # Thêm nút con vừa tạo vào nút hiện tại với giá trị là i (giá trị của thuộc tính được chọn)
        features.insert(index, final_feature)  # Chèn lại thuộc tính đã loại bỏ trước đó vào danh sách thuộc tính
        return current_node  # Trả về nút hiện tại


    # Huấn luyện mô hình trên tập dữ liệu huấn luyện
    def fit(self, X, Y):
        features = [i for i in range(len(X[0]))]  # Tạo danh sách các chỉ số đặc trưng từ 0 đến số lượng đặc trưng trong mỗi mẫu
        classes = set(Y)  # Tìm tập hợp các lớp duy nhất trong nhãn Y
        level = 0  # Khởi tạo mức độ đầu tiên của cây
        # Xây dựng cây quyết định bằng cách gọi phương thức decision_tree
        self.__root = self.decision_tree(X, Y, features, level, "entropy", classes)


    # Phương thức dự đoán đầu ra của một hoặc nhiều mẫu đầu vào
    def predict(self, X):
        # Duyệt qua từng mẫu trong tập dữ liệu đầu vào và gọi phương thức predict_helper để dự đoán đầu ra cho mỗi mẫu
        return [self.predict_helper(x, self.__root) for x in X]

    # Phương thức dự đoán đầu ra cho một mẫu đầu vào
    def predict_helper(self, X, root):
        if len(root.children) == 0:  # Nếu nút là nút lá
            return root.output  # Trả về đầu ra của nút
        # Nếu nút không phải là nút lá, gọi đệ quy để dự đoán đầu ra cho mẫu đầu vào
        if X[root.data] in root.children:
            return self.predict_helper(X, root.children[X[root.data]])
        else:  # Trường hợp này xảy ra khi thuộc tính được chọn trong tập dữ liệu kiểm tra không tồn tại trong tập dữ liệu huấn luyện
            return root.output

# Đọc dữ liệu từ file CSV
data = pd.read_csv("E-commerce Customer Behavior weka.csv")

# Chuẩn bị dữ liệu
X = data.iloc[:, :-1].values  # Lấy các cột từ cột 0 đến cột trước cột cuối
Y = data.iloc[:, -1].values  # Lấy cột cuối cùng
X_train, X_test, Y_train, Y_test = train_test_split(X, Y, test_size=0.3, random_state=42)

# Huấn luyện mô hình
model = DecisionTreeClassifier()
model.fit(X_train, Y_train)

# Dự đoán kết quả trên tập kiểm tra
Y_pred = model.predict(X_test)

# In các chỉ số đánh giá
print("Confusion Matrix:")
print(confusion_matrix(Y_test, Y_pred))
print("\nAccuracy Score:", accuracy_score(Y_test, Y_pred))
print("\nClassification Report:")
print(classification_report(Y_test, Y_pred, zero_division=1))  # Thêm zero_division=1 để xử lý cảnh báo
