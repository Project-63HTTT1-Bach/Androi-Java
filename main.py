import tkinter as tk
from tkinter import filedialog
import pandas as pd
import numpy as np
from tabulate import tabulate
from tkinter import ttk, messagebox
import seaborn as sns
import matplotlib.pyplot as plt
from matplotlib.backends.backend_tkagg import FigureCanvasTkAgg
from sklearn import preprocessing
from apriori import AprioriAlgorithm_AssociationRule
from NaiveBayes import NaiveBayesClassifier
import random

def read_data(file_path):
    data = pd.read_csv(file_path)
    X = data.drop('Satisfaction Level', axis=1).values
    y = data['Satisfaction Level'].values
    feature_names = list(data.columns)[:-1]
    return X, y, feature_names

def run_naive_bayes(file_path):
    global result_text_widget,result_text_widget_created
    if not result_text_widget_created:
        result_text_widget = tk.Text(text_frame, height=30, width=100)
        result_text_widget.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
        result_text_widget_created =True
    X, y, feature_names = read_data(file_path)

    nbc = NaiveBayesClassifier()
    nbc.fit(X, y)

    accuracy, incorrect, kappa, confusion_matrix = nbc.evaluate(X, y)

    result_text = ""

    for c, probabilities in nbc.get_feature_probabilities().items():
        result_text += "Class: {}\n".format(c)
        data = []
        for (feature_index, feature_value), probability in probabilities.items():
            data.append([feature_names[feature_index], feature_value, probability])
        result_text += tabulate(data, headers=['Feature', 'Value', 'Probability'], tablefmt='orgtbl')
        result_text += "\n\n"

    mean_absolute_error = np.mean(np.abs(nbc.predict(X) - y))
    root_mean_squared_error = np.sqrt(np.mean((nbc.predict(X) - y) ** 2))
    relative_absolute_error = mean_absolute_error / np.mean(np.abs(y))
    root_relative_squared_error = root_mean_squared_error / np.mean(np.abs(y))

    detailed_accuracy = pd.DataFrame({
        'Class': np.arange(len(nbc.classes)),
        'TP Rate': np.diag(confusion_matrix) / np.sum(confusion_matrix, axis=1),
        'FP Rate': (np.sum(confusion_matrix, axis=0) - np.diag(confusion_matrix)) / np.sum(confusion_matrix, axis=1),
        'Precision': np.diag(confusion_matrix) / np.sum(confusion_matrix, axis=0),
        'Recall': np.diag(confusion_matrix) / np.sum(confusion_matrix, axis=1),
        'F-Measure': 2 * (np.diag(confusion_matrix) / np.sum(confusion_matrix, axis=0) * np.diag(confusion_matrix) / np.sum(confusion_matrix, axis=1)) / (np.diag(confusion_matrix) / np.sum(confusion_matrix, axis=0) + np.diag(confusion_matrix) / np.sum(confusion_matrix, axis=1)),
        'MCC': ((np.diag(confusion_matrix) * np.sum(confusion_matrix, axis=1) - np.sum(confusion_matrix, axis=0) * np.sum(confusion_matrix, axis=0)) /
                np.sqrt((np.sum(confusion_matrix, axis=0) + np.sum(confusion_matrix, axis=1)) *
                        np.sum(confusion_matrix, axis=0) * np.sum(confusion_matrix, axis=1)))
    })

    result_text_widget.config(state="normal")  # Thiết lập trạng thái văn bản về "normal" để cho phép chỉnh sửa
    result_text_widget.delete("1.0", tk.END)  # Xóa nội dung hiện tại của vùng văn bản
    result_text_widget.insert(tk.END, result_text)
    result_text_widget.insert(tk.END, "=== Stratified cross-validation ===\n")
    result_text_widget.insert(tk.END, "=== Summary ===\n")
    result_text_widget.insert(tk.END, "Correctly Classified Instances         {}               {}%\n".format(accuracy * len(y), accuracy * 100))
    result_text_widget.insert(tk.END, "Incorrectly Classified Instances        {}               {}%\n".format(incorrect, (incorrect / len(y)) * 100))
    result_text_widget.insert(tk.END, "Kappa statistic                          {}\n".format(kappa))
    result_text_widget.insert(tk.END, "Mean absolute error                      {}\n".format(mean_absolute_error))
    result_text_widget.insert(tk.END, "Root mean squared error                  {}\n".format(root_mean_squared_error))
    result_text_widget.insert(tk.END, "Relative absolute error                  {}%\n".format(relative_absolute_error * 100))
    result_text_widget.insert(tk.END, "Root relative squared error              {}%\n".format(root_relative_squared_error * 100))
    result_text_widget.insert(tk.END, "Total Number of Instances                {}\n\n".format(len(y)))
    result_text_widget.insert(tk.END, "=== Detailed Accuracy By Class ===\n\n")
    result_text_widget.insert(tk.END, tabulate(detailed_accuracy, headers='keys', tablefmt='grid'))
    result_text_widget.insert(tk.END, "\n\n=== Confusion Matrix ===\n")
    result_text_widget.insert(tk.END, tabulate(confusion_matrix, headers=['a', 'b', 'c'], tablefmt='orgtbl'))
    result_text_widget.config(state="disabled")  # Thiết lập trạng thái văn bản về "disabled" để ngăn chặn chỉnh sửa
    result_text_widget.pack(padx=20, pady=20, anchor="w", side="top")
def select_file():
    file_path = filedialog.askopenfilename(filetypes=[("CSV files", "*.csv")])
    if file_path:
        file_path_label.config(text=file_path)
def drop_add_data():
        title_label.place_forget()
        input_info_frame.place_forget()
        gender_label.place_forget()
        gender_combobox.place_forget()
        age_label.place_forget()
        age_combobox.place_forget()
        city_label.place_forget()
        city_combobox.place_forget()
        memebershiptype_label.place_forget()
        memebershiptype_combobox.place_forget()
        total_spend_label.place_forget()
        total_spend_combobox.place_forget()
        items_purchased_label.place_forget()
        items_purchased_combobox.place_forget()
        averge_rating_label.place_forget()
        averge_rating_combobox.place_forget()
        discount_applied_label.place_forget()
        discount_applied_combobox.place_forget()
        days_since_last_purchase_label.place_forget()
        days_since_last_purchase_combobox.place_forget()
        button_frame_2.place_forget()
        predict_button.pack_forget()
        random_button.pack_forget()
        result_label.place_forget()        
def run_naive_bayes_command():
    global apriori_widgets_created,result_text_widget,canvas,metrix_widgets_created,result_text_widget_created, add_data_created# Sử dụng biến global view_correlation_button\

    if apriori_widgets_created:
        area.grid_remove()
        minsup_label.grid_remove()
        minsup_entry.grid_remove()
        minconf_label.grid_remove()
        minconf_entry.grid_remove()
        run_button.grid_remove()
        result_text_widget.pack_forget()
        apriori_widgets_created = False
    if metrix_widgets_created:
        metrix_widgets_created = False
        canvas.get_tk_widget().pack_forget()
    if add_data_created:
        drop_add_data()
        add_data_created = False

    file_path = file_path_label.cget("text")
    if not file_path:
        messagebox.showerror("Error", "Please select a CSV file!")
        return
    else:
        run_naive_bayes(file_path)

current_state = "naive_bayes_result"
back_button = None  # Định nghĩa biến back_button là một biến global
canvas = None

def view_correlation_matrix():
    global current_state, back_button, canvas,apriori_widgets_created,minconf_entry,minconf_label,minsup_entry,minsup_label,run_button,result_text_widget_created, metrix_widgets_created, add_data_created # Thêm canvas vào danh sách các biến global
    
    if result_text_widget_created:
        result_text_widget.pack_forget()
        result_text_widget_created = False
    if apriori_widgets_created:
        area.grid_remove()
        minsup_label.grid_remove()
        minsup_entry.grid_remove()
        minconf_label.grid_remove()
        minconf_entry.grid_remove()
        run_button.grid_remove()
        result_text_widget.grid_remove()
        apriori_widgets_created = False
    if add_data_created:
        drop_add_data()
        add_data_created = False
    file_path = file_path_label.cget("text")
    
    le = preprocessing.LabelEncoder()
    data = pd.read_csv(file_path)
    data = data.apply(le.fit_transform)

    corr = data.corr(method="pearson")

    if not metrix_widgets_created:
        # Tạo một figure để vẽ ma trận tương quan
        fig, ax = plt.subplots(figsize=(10, 8))
        cmap = sns.diverging_palette(250, 354, 80, 60, center='dark', as_cmap=True)
        sns.heatmap(corr, vmax=1, vmin=-1, cmap=cmap, square=True, linewidths=.2, ax=ax)
        
        # Nhúng hình vào giao diện Tkinter
        canvas = FigureCanvasTkAgg(fig, master=text_frame)
        canvas.draw()
        canvas.get_tk_widget().pack(padx=20, pady=20, anchor="w", side="top")
        metrix_widgets_created = True
    # Gán trạng thái hiện tại là ma trận tương quan

def run_apriori():
    global result_frame,minconf_entry,minconf_label,minsup_entry,minsup_label,run_button,result_text_widget,apriori_widgets_created,canvas,result_text_widget_created,metrix_widgets_created, add_data_created, area
    if result_text_widget_created:
        result_text_widget.pack_forget()
        result_text_widget_created = False
    if metrix_widgets_created:
        metrix_widgets_created =False
        canvas.get_tk_widget().pack_forget()
    if add_data_created:
        drop_add_data()
        add_data_created = False

    if not apriori_widgets_created:
        area = ttk.Label(text_frame, text="")
        area.grid(row=0, column=0, padx=100, pady=10, sticky="w")

        minsup_label = ttk.Label(text_frame, text="minsup:")
        minsup_label.grid(row=0, column=1, padx=20, pady=10, sticky="w")

        minsup_entry = ttk.Entry(text_frame)
        minsup_entry.grid(row=0, column=2, padx=20, pady=10, sticky="w")

        minconf_label = ttk.Label(text_frame, text="minconf:")
        minconf_label.grid(row=1, column=1, padx=20, pady=10, sticky="w")

        minconf_entry = ttk.Entry(text_frame)
        minconf_entry.grid(row=1, column=2, padx=20, pady=10, sticky="w")

        run_button = ttk.Button(text_frame, text="Run Apriori", command=Run_button)
        run_button.grid(row=2, column=1, padx=20, pady=10, sticky="w")

        apriori_widgets_created = True

def Run_button():
    global minconf_entry,minconf_label,minsup_entry,minsup_label,run_button,result_text_widget,apriori_widgets_created,result_text_widget_created,result_frame
    file_path = file_path_label.cget("text")
    area.grid_remove()
    minsup_label.grid_remove()
    minsup_entry.grid_remove()
    minconf_label.grid_remove()
    minconf_entry.grid_remove()
    run_button.grid_remove()
    apriori_widgets_created = False
    result_text_widget = tk.Text(text_frame, height=30, width=100)
    result_text_widget.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
    result_text_widget_created = True
    if not file_path:
        messagebox.showerror("Error", "Please select a CSV file!")
        return
    
    data = pd.read_csv(file_path)
    min_supp = float(minsup_entry.get())  # Lấy giá trị từ ô nhập minsup
    min_conf = float(minconf_entry.get())  # Lấy giá trị từ ô nhập minconf
    
    apriori = AprioriAlgorithm_AssociationRule(min_supp, min_conf)
    apriori.find_rules(data)

    frequent_itemsets = apriori.get_frequent_itemsets()
    association_rules = apriori.get_association_rules()

    result_text = "=== Frequent Itemsets ===\n"
    for i in range(len(frequent_itemsets)):
        result_text +="\nL" + str(i+1) + "\n"
        for item in frequent_itemsets[i]:
            result_text += str(item) + "\n"
        result_text += "\nSize of set of large itemsets L"+ str(i + 1) + " : "+str(len(frequent_itemsets[i])) + "\n"
    result_text += "\n\n"
    result_text += "=== Association Rules ===\n"
    for rule in association_rules:
        result_text += str(rule[0]) + " -> " + str(rule[1]) + "   conf: " + str(rule[2]) + "\n"

    result_text_widget.delete("1.0", tk.END)
    result_text_widget.insert(tk.END, result_text)
    result_text_widget.pack(padx=20, pady=20, anchor="w", side="top")

def random_selection():
    gender_combobox.set(random.choice(["Male", "Female"]))
    age_combobox.set(random.choice(["\'Young Adults\'", "\'Middle-Aged Adults\'"]))
    city_combobox.set(random.choice(["\'Los Angeles\'", "Chicago", "\'San Francisco\'", "Miami", "\'New York\'", "Houston"]))
    memebershiptype_combobox.set(random.choice(["Silver","Bronze","Gold"]))
    total_spend_combobox.set(random.choice(["\'Low Spending\'", "\'Average Spending\'", "\'High Spending\'"]))
    items_purchased_combobox.set(random.choice(["\'Little Product\'", "\'Average Product\'", "\'Many Product\'"]))
    averge_rating_combobox.set(random.choice(["\'Low Rating\'", "\'Average Rating\'", "\'High Rating\'"]))
    discount_applied_combobox.set(random.choice(["True", "False"]))
    days_since_last_purchase_combobox.set(random.choice(["\'Recent Purchase\'", "\'Regular Purchase\'", "\'Infrequent Purchase\'"]))
    clear_result_label()

def add_data():
    global gender_combobox, age_combobox, city_combobox, memebershiptype_combobox, total_spend_combobox
    global items_purchased_combobox, averge_rating_combobox, discount_applied_combobox, days_since_last_purchase_combobox
    global result_text_widget,result_text_widget_created, apriori_widgets_created, metrix_widgets_created, add_data_created
    global title_label, input_info_frame, gender_label, gender_combobox, age_label, age_combobox, city_label, city_combobox, memebershiptype_label, memebershiptype_combobox, total_spend_label, total_spend_combobox, items_purchased_label, items_purchased_combobox
    global averge_rating_label, averge_rating_combobox, discount_applied_label, discount_applied_combobox, days_since_last_purchase_label, days_since_last_purchase_combobox, button_frame_2, predict_button, random_button

    if apriori_widgets_created:
        area.grid_remove()
        minsup_label.grid_remove()
        minsup_entry.grid_remove()
        minconf_label.grid_remove()
        minconf_entry.grid_remove()
        run_button.grid_remove()
        result_text_widget.pack_forget()
        apriori_widgets_created = False
    if metrix_widgets_created:
        metrix_widgets_created = False
        canvas.get_tk_widget().pack_forget()
    if result_text_widget_created:
        result_text_widget.pack_forget()
        result_text_widget_created = False
    if not add_data_created:
        title_label = ttk.Label(text_frame, text="DỰ ĐOÁN MỨC ĐỘ HÀI LÒNG KHÁCH HÀNG", font=("Helvetica", 16, "bold"), foreground="red")
        title_label.place(relx=0.5, rely=0.15, anchor=tk.CENTER)

        custom_font = ('Arial', 12, 'bold')

        style = ttk.Style()
        style.configure("Bold.TLabelframe.Label", font=custom_font)

        input_info_frame = ttk.LabelFrame(text_frame, text="Thông tin nhập", style="Bold.TLabelframe")
        input_info_frame.place(relx=0.05, rely=0.2, relwidth=0.9, relheight=0.55)

        gender_label = ttk.Label(text_frame, text="Gender:")
        gender_label.place(relx=0.1, rely=0.27, anchor=tk.W)

        gender_combobox = ttk.Combobox(text_frame, values=["Male", "Female"])
        gender_combobox.place(relx=0.32, rely=0.27, anchor=tk.W)

        age_label = ttk.Label(text_frame, text="Age:")
        age_label.place(relx=0.1, rely=0.37, anchor=tk.W)

        age_combobox = ttk.Combobox(text_frame, values=["\'Young Adults\'", "\'Middle-Aged Adults\'"])
        age_combobox.place(relx=0.32, rely=0.37, anchor=tk.W)

        def update_age_label(*args):
            selected_age = age_combobox.get()
            if selected_age == "\'Young Adults\'":
                age_label.config(text="Age: 26-35 tuổi", foreground="black")
            elif selected_age == "\'Middle-Aged Adults\'":
                age_label.config(text="Age: 36-43 tuổi", foreground="black")
            else:
                age_label.config(text="Age:", foreground="black")

        age_combobox.bind("<<ComboboxSelected>>", update_age_label)

        city_label = ttk.Label(text_frame, text="City:")
        city_label.place(relx=0.1, rely=0.47, anchor=tk.W)

        city_combobox = ttk.Combobox(text_frame, values=["\'Los Angeles\'", "Chicago", "\'San Francisco\'", "Miami", "\'New York\'", "Houston"])
        city_combobox.place(relx=0.32, rely=0.47, anchor=tk.W)

        memebershiptype_label = ttk.Label(text_frame, text="Membership Type:")
        memebershiptype_label.place(relx=0.1, rely=0.57, anchor=tk.W)

        memebershiptype_combobox = ttk.Combobox(text_frame, values=["Silver","Bronze","Gold"])
        memebershiptype_combobox.place(relx=0.32, rely=0.57, anchor=tk.W)

        total_spend_label = ttk.Label(text_frame, text="ToTalSpend:")
        total_spend_label.place(relx=0.1, rely=0.67, anchor=tk.W)

        total_spend_combobox = ttk.Combobox(text_frame, values=["\'Low Spending\'", "\'Average Spending\'", "\'High Spending\'"])
        total_spend_combobox.place(relx=0.32, rely=0.67, anchor=tk.W)

        def update_total_spend_label(*args):
            selected_total_spend = total_spend_combobox.get()
            if selected_total_spend == "\'Low Spending\'":
                total_spend_label.config(text="ToTalSpend: 410k - 600k", foreground="black")
            elif selected_total_spend == "\'Average Spending\'":
                total_spend_label.config(text="ToTalSpend: 601k - 1000tr", foreground="black")
            elif selected_total_spend == "\'High Spending\'":
                total_spend_label.config(text="ToTalSpend: 1001tr - 1520tr", foreground="black")
            else:
                total_spend_label.config(text="ToTalSpend:", foreground="black")

        total_spend_combobox.bind("<<ComboboxSelected>>", update_total_spend_label)

        items_purchased_label = ttk.Label(text_frame, text="Items Purchased:")
        items_purchased_label.place(relx=0.55, rely=0.27, anchor=tk.W)

        items_purchased_combobox = ttk.Combobox(text_frame, values=["\'Little Product\'", "\'Average Product\'", "\'Many Product\'"])
        items_purchased_combobox.place(relx=0.75, rely=0.27, anchor=tk.W)

        def update_items_purchased_label(*args):
            selected_items_purchased = items_purchased_combobox.get()
            if selected_items_purchased == "\'Little Product\'":
                items_purchased_label.config(text="Items Purchased: 2 - 5", foreground="black")
            elif selected_items_purchased == "\'Average Product\'":
                items_purchased_label.config(text="Items Purchased: 6 - 10", foreground="black")
            elif selected_items_purchased == "\'Many Product\'":
                items_purchased_label.config(text="Items Purchased: 11 - 21", foreground="black")
            else:
                items_purchased_label.config(text="Items Purchased:", foreground="black")

        items_purchased_combobox.bind("<<ComboboxSelected>>", update_items_purchased_label)

        averge_rating_label = ttk.Label(text_frame, text="Average Rating:")
        averge_rating_label.place(relx=0.55, rely=0.37, anchor=tk.W)

        averge_rating_combobox = ttk.Combobox(text_frame, values=["\'Low Rating\'", "\'Average Rating\'", "\'High Rating\'"])
        averge_rating_combobox.place(relx=0.75, rely=0.37, anchor=tk.W)

        def update_average_rating_label(*args):
            selected_option = averge_rating_combobox.get()
            if selected_option == "\'Low Rating\'":
                averge_rating_label.config(text="Average Rating: 3 - 3.9", foreground="black")
            elif selected_option == "\'Average Rating\'":
                averge_rating_label.config(text="Average Rating: 4 - 4.4", foreground="black")
            elif selected_option == "\'High Rating\'":
                averge_rating_label.config(text="Average Rating: 4.5 - 4.9", foreground="black")
            else:
                averge_rating_label.config(text="Average Rating:", foreground="black")

        averge_rating_combobox.bind("<<ComboboxSelected>>", update_average_rating_label)

        discount_applied_label = ttk.Label(text_frame, text="Discount Applied: ")
        discount_applied_label.place(relx=0.55, rely=0.47, anchor=tk.W)

        discount_applied_combobox = ttk.Combobox(text_frame, values=["True", "False"])
        discount_applied_combobox.place(relx=0.75, rely=0.47, anchor=tk.W)

        days_since_last_purchase_label = ttk.Label(text_frame, text="Days Since Last Purchase:")
        days_since_last_purchase_label.place(relx=0.55, rely=0.57, anchor=tk.W)

        days_since_last_purchase_combobox = ttk.Combobox(text_frame, values=["\'Recent Purchase\'", "\'Regular Purchase\'", "\'Infrequent Purchase\'"])
        days_since_last_purchase_combobox.place(relx=0.55, rely=0.62, anchor=tk.W)

        def update_days_since_last_purchase_label(*args):
            selected_option = days_since_last_purchase_combobox.get()
            if selected_option == "\'Recent Purchase\'":
                days_since_last_purchase_label.config(text="Days Since Last Purchase: 9 - 21 days", foreground="black")
            elif selected_option == "\'Regular Purchase\'":
                days_since_last_purchase_label.config(text="Days Since Last Purchase: 22 - 42 days", foreground="black")
            elif selected_option == "\'Infrequent Purchase\'":
                days_since_last_purchase_label.config(text="Days Since Last Purchase: 43 - 63 days", foreground="black")
            else:
                days_since_last_purchase_label.config(text="Days Since Last Purchase:", foreground="black")

        days_since_last_purchase_combobox.bind("<<ComboboxSelected>>", update_days_since_last_purchase_label)

        button_frame_2 = ttk.Frame(root)
        button_frame_2.place(relx=0.5, rely=0.8, anchor=tk.CENTER)

        predict_button = ttk.Button(button_frame_2, text="Dự đoán kết quả", width=20, command=predict_satisfaction_level)
        predict_button.pack(side=tk.LEFT)

        random_button = ttk.Button(button_frame_2, text="Random", width=20, command=random_selection)
        random_button.pack(side=tk.LEFT, padx=10)
    add_data_created = True
    random_selection()
    predict_satisfaction_level()
def clear_result_label():
    global result_label
    if result_label:
        result_label.destroy()

def predict_satisfaction_level():
    global result_label  # Sử dụng biến result_label ở global scope để có thể cập nhật trạng thái hiển thị trên giao diện

    # Lấy giá trị từ các combobox cho các thuộc tính
    gender = gender_combobox.get()
    age = age_combobox.get()
    city = city_combobox.get()
    membership_type = memebershiptype_combobox.get()
    total_spend = total_spend_combobox.get()
    items_purchased = items_purchased_combobox.get()
    average_rating = averge_rating_combobox.get()
    discount_applied = discount_applied_combobox.get()
    days_since_last_purchase = days_since_last_purchase_combobox.get()

    # Kiểm tra xem có bất kỳ trường nào để trống không
    if any(value == "" for value in [gender, age, city, membership_type, total_spend, items_purchased, average_rating, discount_applied, days_since_last_purchase]):
        messagebox.showerror("Lỗi", "Vui lòng chọn giá trị cho tất cả các trường")  # Hiển thị thông báo lỗi nếu có trường trống
        return

    # Đọc dữ liệu từ tệp CSV
    data = pd.read_csv("E-commerce Customer Behavior weka.csv")

    # Tạo mô hình phân loại Naive Bayes và huấn luyện
    classifier = NaiveBayesClassifier()
    X_train = data.iloc[:, :-1].values
    y_train = data.iloc[:, -1].values

    classifier.fit(X_train, y_train)

    # Dự đoán mức độ hài lòng cho dữ liệu mới
    X_data = pd.DataFrame([gender, age, city, membership_type, total_spend, items_purchased, average_rating, discount_applied, days_since_last_purchase]).transpose()
    X_data = X_data.iloc[:, :].values
    satisfaction_level = classifier.predict(X_data)
    # Tạo văn bản kết quả dự đoán
    if satisfaction_level == [0]:
        result_text = "0 - Không hài lòng"
    elif satisfaction_level == [1]:
        result_text = "1 - Hài lòng"
    elif satisfaction_level == [2]:
        result_text = "2 - Rất hài lòng"
    # Tạo và hiển thị nhãn kết quả dự đoán trên giao diện
    clear_result_label()
    result_label = ttk.Label(root, text=f"Mức độ hài lòng dự đoán: {result_text}")
    result_label.place(relx=0.5, rely=0.85, anchor=tk.CENTER)

# Create the root window
root = tk.Tk()
root.title("Nhom 7")
x = 800
y = 600
root.geometry(f"{x}x{y}+{x-400}+{y-500}")

button_frame = ttk.Frame(root)
button_frame.pack(pady=20)

info_frame = ttk.Frame(root)
info_frame.pack(pady=5)

result_frame = ttk.Frame(root)
result_frame.pack(pady=5)

text_frame = ttk.Frame(root)
text_frame.pack(fill=tk.BOTH, expand=True, padx=20, pady=5)

# Create the "Select File" button
select_button = ttk.Button(button_frame, text="Select File", command=select_file, width=20)
select_button.pack(side=tk.LEFT, padx=(0, 10), pady=5)

# Create the "Run Naive Bayes" button
run_button = ttk.Button(button_frame, text="Run Naive Bayes", command=run_naive_bayes_command, width=20)
run_button.pack(side=tk.LEFT, padx=(0, 10), pady=5)

# Create the "Correlation Matrix" button
correlation_matrix_button = ttk.Button(button_frame, text="Correlation Matrix", command=view_correlation_matrix, width=20)
correlation_matrix_button.pack(side=tk.LEFT, padx=(0, 10), pady=5)

# Create the "Apriori" button
apriori_button = ttk.Button(button_frame, text="Apriori", command=run_apriori, width=20)
apriori_button.pack(side=tk.LEFT, padx=(0, 10), pady=5)

# Create the "Add Data" button
add_data_button = ttk.Button(button_frame, text="Add Data", command=add_data, width=20)
add_data_button.pack(side=tk.LEFT, padx=(0, 10), pady=5)

# Display the selected file path
file_path_label = ttk.Label(info_frame, text="", wraplength=600)
file_path_label.grid(row=0, column=0, padx=20, pady=10, sticky="w")

# Create frame for view button
view_button_frame = ttk.Frame(info_frame)
view_button_frame.grid(row=0, column=1, pady=5, sticky="e")

# Tạo vùng văn bản hiển thị kết quả
result_text_widget = tk.Text(text_frame, height=30, width=100)
result_text_widget.pack(side=tk.LEFT, fill=tk.BOTH, expand=True)
result_text_widget.pack_forget()
# Tạo kết quả dự đoán
result_label = ""


apriori_widgets_created = False
metrix_widgets_created = False
result_text_widget_created = True
add_data_created = False
# Run the GUI
root.mainloop()

# test Bach