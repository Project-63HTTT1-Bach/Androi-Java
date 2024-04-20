import pandas as pd
import numpy as np
from collections import defaultdict
from tabulate import tabulate

class NaiveBayesClassifier:
    def __init__(self):
        # Phương thức khởi tạo được gọi khi một đối tượng mới của lớp được tạo ra
        # Khởi tạo biến thành viên class_probabilities với giá trị None
        self.class_probabilities = None
        
        # Khởi tạo biến thành viên feature_probabilities với giá trị None
        self.feature_probabilities = None

    def fit(self, X, y):
        # Lấy số lượng mẫu và số lượng đặc trưng từ dữ liệu đầu vào
        n_samples, n_features = X.shape
        
        # Xác định các lớp duy nhất trong tập dữ liệu và số lượng lớp
        self.classes = np.unique(y)
        n_classes = len(self.classes)

        # Khởi tạo một từ điển để lưu trữ xác suất cho từng lớp
        self.class_probabilities = defaultdict(float)
        
        # Khởi tạo một từ điển lồng để lưu trữ xác suất của từng thuộc tính cho mỗi lớp
        self.feature_probabilities = defaultdict(lambda: defaultdict(float))

        # Tính xác suất của từng lớp
        for c in self.classes:
            # Tạo mặt nạ để chọn các mẫu thuộc lớp c hiện tại
            mask = (y == c)
            
            # Tính xác suất của lớp c sử dụng Laplace Smoothing
            self.class_probabilities[c] = (np.sum(mask) + 1) / (n_samples + n_classes)  # Laplace Smoothing

            # Tính xác suất của từng thuộc tính cho mỗi lớp
            for i in range(n_features):
                # Lấy các giá trị duy nhất của đặc trưng thứ i
                feature_values = np.unique(X[:, i])
                
                # Tính xác suất của giá trị feature cho lớp c
                for value in feature_values:
                    # Tạo mặt nạ để chọn các mẫu có giá trị feature là value
                    feature_mask = (X[:, i] == value)
                    
                    # Tính xác suất của giá trị feature là value cho lớp c sử dụng Laplace Smoothing
                    self.feature_probabilities[c][i, value] = (np.sum(mask & feature_mask) + 1) / (np.sum(mask) + len(feature_values))  # Laplace Smoothing


    def predict(self, X):
        # Kiểm tra xem mô hình đã được huấn luyện chưa
        if self.class_probabilities is None or self.feature_probabilities is None:
            raise RuntimeError("Classifier has not been trained.")

        # Lấy số lượng mẫu và số lượng đặc trưng từ dữ liệu đầu vào
        n_samples, n_features = X.shape
        predictions = []

        # Dự đoán cho từng mẫu
        for i in range(n_samples):
            # Lấy mẫu thứ i
            sample = X[i, :]
            
            # Tạo một từ điển để lưu trữ điểm số cho từng lớp
            class_scores = defaultdict(float)

            # Tính điểm số cho từng lớp
            for c in self.classes:
                # Điểm số cho lớp c bằng log của xác suất lớp c
                class_scores[c] = np.log(self.class_probabilities[c])

                # Tính điểm số cho từng thuộc tính của mẫu
                for j in range(n_features):
                    feature_value = sample[j]
                    if (j, feature_value) in self.feature_probabilities[c]:
                    # Cộng thêm điểm số của giá trị đặc trưng vào điểm số của lớp c
                        class_scores[c] += np.log(self.feature_probabilities[c][j, feature_value])

            # Chọn lớp có điểm số cao nhất là kết quả dự đoán cho mẫu thứ i
            predicted_class = max(class_scores, key=class_scores.get)
            predictions.append(predicted_class)

        return predictions


    def get_feature_probabilities(self):
        if self.feature_probabilities is None:
            raise RuntimeError("Classifier has not been trained.")

        return self.feature_probabilities

    def evaluate(self, X, y):
        y_pred = self.predict(X)

        # Tính toán các chỉ số đánh giá
        n_samples = len(y)
        correct = np.sum(y_pred == y)
        incorrect = n_samples - correct
        accuracy = correct / n_samples
        kappa = (accuracy - (1 / len(self.classes))) / (1 - (1 / len(self.classes)))

        # Tính toán ma trận nhầm lẫn
        confusion_matrix = np.zeros((len(self.classes), len(self.classes)), dtype=int)
        for true_class, pred_class in zip(y, y_pred):
            confusion_matrix[true_class][pred_class] += 1

        return accuracy, incorrect, kappa, confusion_matrix
    
# Đọc dữ liệu từ file CSV
def read_data(file_path):
    data = pd.read_csv(file_path)
    X = data.drop('Satisfaction Level', axis=1).values
    y = data['Satisfaction Level'].values
    feature_names = list(data.columns)[:-1]
    return X, y, feature_names

# Chương trình chính
def main():
    # Đường dẫn đến file CSV
    file_path = 'E-commerce Customer Behavior weka.csv'

    # Đọc dữ liệu từ file CSV
    X, y, feature_names = read_data(file_path)

    # Tạo mô hình Naive Bayes và huấn luyện
    nbc = NaiveBayesClassifier()
    nbc.fit(X, y)

    # Dự đoán nhãn cho dữ liệu huấn luyện và đánh giá mô hình
    accuracy, incorrect, kappa, confusion_matrix = nbc.evaluate(X, y)

    # Hiển thị kết quả với tên cột
    for c, probabilities in nbc.get_feature_probabilities().items():
        print("Class:", c)
        data = []
        for (feature_index, feature_value), probability in probabilities.items():
            data.append([feature_names[feature_index], feature_value, probability])
        print(tabulate(data, headers=['Feature', 'Value', 'Probability'], tablefmt='orgtbl'))

    # Tính các chỉ số đánh giá bổ sung
    mean_absolute_error = np.mean(np.abs(nbc.predict(X) - y))
    root_mean_squared_error = np.sqrt(np.mean((nbc.predict(X) - y) ** 2))
    relative_absolute_error = mean_absolute_error / np.mean(np.abs(y))
    root_relative_squared_error = root_mean_squared_error / np.mean(np.abs(y))

    # Hiển thị kết quả đánh giá
    print("=== Stratified cross-validation ===")
    print("=== Summary ===")
    print("Correctly Classified Instances         ", accuracy * len(y), "               ", accuracy * 100, "%")
    print("Incorrectly Classified Instances        ", incorrect, "               ", (incorrect / len(y)) * 100, "%")

    print("\n=== Confusion Matrix ===\n")
    print(tabulate(confusion_matrix, headers=['a', 'b', 'c'], tablefmt='orgtbl'))

if __name__ == '__main__':
    main()

