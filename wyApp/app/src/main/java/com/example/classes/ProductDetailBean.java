package com.example.classes;

import java.util.List;

/**
 * Created by NY on 2017/2/5.
 * 商品详情
 */

public class ProductDetailBean {

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

        private ProductInfoBean ProductInfo;
        private long EstimateTime;
        private String TypeInstruction;
        private String ToxicityInstruction;
        private String RegistrationNumber;
        private String ApprovalNumber;
        private String StandardNumber;
        private String ProFreightRule;
        private String ManufPhone;
        private List<String> ProImgList;

        private List<ProductSpecListBean> ProductSpecList;

        public ProductInfoBean getProductInfo() {
            return ProductInfo;
        }

        public void setProductInfo(ProductInfoBean ProductInfo) {
            this.ProductInfo = ProductInfo;
        }

        public long getEstimateTime() {
            return EstimateTime;
        }

        public void setEstimateTime(long EstimateTime) {
            this.EstimateTime = EstimateTime;
        }

        public String getTypeInstruction() {
            return TypeInstruction;
        }

        public void setTypeInstruction(String TypeInstruction) {
            this.TypeInstruction = TypeInstruction;
        }

        public String getToxicityInstruction() {
            return ToxicityInstruction;
        }

        public void setToxicityInstruction(String ToxicityInstruction) {
            this.ToxicityInstruction = ToxicityInstruction;
        }

        public String getRegistrationNumber() {
            return RegistrationNumber;
        }

        public void setRegistrationNumber(String RegistrationNumber) {
            this.RegistrationNumber = RegistrationNumber;
        }

        public String getApprovalNumber() {
            return ApprovalNumber;
        }

        public void setApprovalNumber(String ApprovalNumber) {
            this.ApprovalNumber = ApprovalNumber;
        }

        public String getStandardNumber() {
            return StandardNumber;
        }

        public void setStandardNumber(String StandardNumber) {
            this.StandardNumber = StandardNumber;
        }

        public String getProFreightRule() {
            return ProFreightRule;
        }

        public void setProFreightRule(String ProFreightRule) {
            this.ProFreightRule = ProFreightRule;
        }

        public String getManufPhone() {
            return ManufPhone;
        }

        public void setManufPhone(String ManufPhone) {
            this.ManufPhone = ManufPhone;
        }

        public List<String> getProImgList() {
            return ProImgList;
        }

        public void setProImgList(List<String> ProImgList) {
            this.ProImgList = ProImgList;
        }

        public List<ProductSpecListBean> getProductSpecList() {
            return ProductSpecList;
        }

        public void setProductSpecList(List<ProductSpecListBean> ProductSpecList) {
            this.ProductSpecList = ProductSpecList;
        }

        public static class ProductInfoBean {
            private int Id;
            private String Pro_Name;
            private int State;
            private String Common_Name;
            private String Total_Content;
            private String Percentage;
            private String Purpose;
            private String Spec;
            private String Dosageform;
            private int Manuf_Id;
            private String Manuf_Name;
            private int Type;
            private int Kind;
            private int Stock;
            private int Marketing_Type;
            private String Begin_Time;
            private String End_Time;

            public int getId() {
                return Id;
            }

            public void setId(int Id) {
                this.Id = Id;
            }

            public String getPro_Name() {
                return Pro_Name;
            }

            public void setPro_Name(String Pro_Name) {
                this.Pro_Name = Pro_Name;
            }

            public int getState() {
                return State;
            }

            public void setState(int State) {
                this.State = State;
            }

            public String getCommon_Name() {
                return Common_Name;
            }

            public void setCommon_Name(String Common_Name) {
                this.Common_Name = Common_Name;
            }

            public String getTotal_Content() {
                return Total_Content;
            }

            public void setTotal_Content(String Total_Content) {
                this.Total_Content = Total_Content;
            }

            public String getPercentage() {
                return Percentage;
            }

            public void setPercentage(String Percentage) {
                this.Percentage = Percentage;
            }

            public String getPurpose() {
                return Purpose;
            }

            public void setPurpose(String Purpose) {
                this.Purpose = Purpose;
            }

            public String getSpec() {
                return Spec;
            }

            public void setSpec(String Spec) {
                this.Spec = Spec;
            }

            public String getDosageform() {
                return Dosageform;
            }

            public void setDosageform(String Dosageform) {
                this.Dosageform = Dosageform;
            }

            public int getManuf_Id() {
                return Manuf_Id;
            }

            public void setManuf_Id(int Manuf_Id) {
                this.Manuf_Id = Manuf_Id;
            }

            public String getManuf_Name() {
                return Manuf_Name;
            }

            public void setManuf_Name(String Manuf_Name) {
                this.Manuf_Name = Manuf_Name;
            }

            public int getType() {
                return Type;
            }

            public void setType(int Type) {
                this.Type = Type;
            }

            public int getKind() {
                return Kind;
            }

            public void setKind(int Kind) {
                this.Kind = Kind;
            }

            public int getStock() {
                return Stock;
            }

            public void setStock(int Stock) {
                this.Stock = Stock;
            }

            public int getMarketing_Type() {
                return Marketing_Type;
            }

            public void setMarketing_Type(int Marketing_Type) {
                this.Marketing_Type = Marketing_Type;
            }

            public String getBegin_Time() {
                return Begin_Time;
            }

            public void setBegin_Time(String Begin_Time) {
                this.Begin_Time = Begin_Time;
            }

            public String getEnd_Time() {
                return End_Time;
            }

            public void setEnd_Time(String End_Time) {
                this.End_Time = End_Time;
            }
        }

        public static class ProductSpecListBean {
            private int ProductId;
            private String BoxPriceRange;
            private String BoxUnit;
            private String BottlePriceRange;
            private String ProSpec;
            private double strSinglesDay;
            private int ProductTypes;
            private String TextDescription;

            private List<PriceRuleListBean> PriceRuleList;

            public int getProductId() {
                return ProductId;
            }

            public void setProductId(int ProductId) {
                this.ProductId = ProductId;
            }

            public String getBoxPriceRange() {
                return BoxPriceRange;
            }

            public void setBoxPriceRange(String BoxPriceRange) {
                this.BoxPriceRange = BoxPriceRange;
            }

            public String getBoxUnit() {
                return BoxUnit;
            }

            public void setBoxUnit(String BoxUnit) {
                this.BoxUnit = BoxUnit;
            }

            public String getBottlePriceRange() {
                return BottlePriceRange;
            }

            public void setBottlePriceRange(String BottlePriceRange) {
                this.BottlePriceRange = BottlePriceRange;
            }

            public String getProSpec() {
                return ProSpec;
            }

            public void setProSpec(String ProSpec) {
                this.ProSpec = ProSpec;
            }

            public double getStrSinglesDay() {
                return strSinglesDay;
            }

            public void setStrSinglesDay(double strSinglesDay) {
                this.strSinglesDay = strSinglesDay;
            }

            public int getProductTypes() {
                return ProductTypes;
            }

            public void setProductTypes(int ProductTypes) {
                this.ProductTypes = ProductTypes;
            }

            public String getTextDescription() {
                return TextDescription;
            }

            public void setTextDescription(String TextDescription) {
                this.TextDescription = TextDescription;
            }

            public List<PriceRuleListBean> getPriceRuleList() {
                return PriceRuleList;
            }

            public void setPriceRuleList(List<PriceRuleListBean> PriceRuleList) {
                this.PriceRuleList = PriceRuleList;
            }

            public static class PriceRuleListBean {
                private String RulePrice;
                private String RuleBottolePrice;
                private int RuleMinCount;
                private int RuleMaxCount;
                private String RulePriceInfo;
                private String Title;

                public String getRulePrice() {
                    return RulePrice;
                }

                public void setRulePrice(String RulePrice) {
                    this.RulePrice = RulePrice;
                }

                public String getRuleBottolePrice() {
                    return RuleBottolePrice;
                }

                public void setRuleBottolePrice(String RuleBottolePrice) {
                    this.RuleBottolePrice = RuleBottolePrice;
                }

                public int getRuleMinCount() {
                    return RuleMinCount;
                }

                public void setRuleMinCount(int RuleMinCount) {
                    this.RuleMinCount = RuleMinCount;
                }

                public int getRuleMaxCount() {
                    return RuleMaxCount;
                }

                public void setRuleMaxCount(int RuleMaxCount) {
                    this.RuleMaxCount = RuleMaxCount;
                }

                public String getRulePriceInfo() {
                    return RulePriceInfo;
                }

                public void setRulePriceInfo(String RulePriceInfo) {
                    this.RulePriceInfo = RulePriceInfo;
                }

                public String getTitle() {
                    return Title;
                }

                public void setTitle(String Title) {
                    this.Title = Title;
                }
            }
        }
    }
}
