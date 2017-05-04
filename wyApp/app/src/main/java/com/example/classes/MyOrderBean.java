package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/1/19.
 * 我的订单bean
 */

public class MyOrderBean {

    private DataBean Data;
    private boolean Result;
    private String Message;

    public DataBean getData() {
        return Data;
    }

    public void setData(DataBean Data) {
        this.Data = Data;
    }

    public boolean isResult() {
        return Result;
    }

    public void setResult(boolean Result) {
        this.Result = Result;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String Message) {
        this.Message = Message;
    }

    public static class DataBean {

        private List<OrdersBean> Orders;

        public List<OrdersBean> getOrders() {
            return Orders;
        }

        public void setOrders(List<OrdersBean> Orders) {
            this.Orders = Orders;
        }

        public static class OrdersBean {

            private OrderGroupBean orderGroup;
            private List<OrderItemsBean> OrderItems;

            public OrderGroupBean getOrderGroup() {
                return orderGroup;
            }

            public void setOrderGroup(OrderGroupBean orderGroup) {
                this.orderGroup = orderGroup;
            }

            public List<OrderItemsBean> getOrderItems() {
                return OrderItems;
            }

            public void setOrderItems(List<OrderItemsBean> OrderItems) {
                this.OrderItems = OrderItems;
            }

            public static class OrderGroupBean {
                private int Id;
                private String Group_No;
                private String User_Name;
                private String Add_Date;
                private int iCount;

                public int getId() {
                    return Id;
                }

                public void setId(int Id) {
                    this.Id = Id;
                }

                public String getGroup_No() {
                    return Group_No;
                }

                public void setGroup_No(String Group_No) {
                    this.Group_No = Group_No;
                }

                public String getUser_Name() {
                    return User_Name;
                }

                public void setUser_Name(String User_Name) {
                    this.User_Name = User_Name;
                }

                public String getAdd_Date() {
                    return Add_Date;
                }

                public void setAdd_Date(String Add_Date) {
                    this.Add_Date = Add_Date;
                }

                public int getICount() {
                    return iCount;
                }

                public void setICount(int iCount) {
                    this.iCount = iCount;
                }
            }

            public static class OrderItemsBean {
                private int Id;
                private String State;
                private double Price;
                private String Send_Name;
                private String ShareLink;
                private List<SubOrdersBean> SubOrders;
                private List<Integer> Buttons;

                public int getId() {
                    return Id;
                }

                public void setId(int Id) {
                    this.Id = Id;
                }

                public String getState() {
                    return State;
                }

                public void setState(String State) {
                    this.State = State;
                }

                public double getPrice() {
                    return Price;
                }

                public void setPrice(double Price) {
                    this.Price = Price;
                }

                public String getSend_Name() {
                    return Send_Name;
                }

                public void setSend_Name(String Send_Name) {
                    this.Send_Name = Send_Name;
                }

                public String getShareLink() {
                    return ShareLink;
                }

                public void setShareLink(String ShareLink) {
                    this.ShareLink = ShareLink;
                }

                public List<SubOrdersBean> getSubOrders() {
                    return SubOrders;
                }

                public void setSubOrders(List<SubOrdersBean> SubOrders) {
                    this.SubOrders = SubOrders;
                }

                public List<Integer> getButtons() {
                    return Buttons;
                }

                public void setButtons(List<Integer> Buttons) {
                    this.Buttons = Buttons;
                }

                public static class SubOrdersBean {
                    private int Id;
                    private String ImageUrl;
                    private String Name;
                    private String Spec;
                    private double Price;
                    private int Count;

                    public int getId() {
                        return Id;
                    }

                    public void setId(int Id) {
                        this.Id = Id;
                    }

                    public String getImageUrl() {
                        return ImageUrl;
                    }

                    public void setImageUrl(String ImageUrl) {
                        this.ImageUrl = ImageUrl;
                    }

                    public String getName() {
                        return Name;
                    }

                    public void setName(String Name) {
                        this.Name = Name;
                    }

                    public String getSpec() {
                        return Spec;
                    }

                    public void setSpec(String Spec) {
                        this.Spec = Spec;
                    }

                    public double getPrice() {
                        return Price;
                    }

                    public void setPrice(double Price) {
                        this.Price = Price;
                    }

                    public int getCount() {
                        return Count;
                    }

                    public void setCount(int Count) {
                        this.Count = Count;
                    }
                }
            }
        }
    }
}
