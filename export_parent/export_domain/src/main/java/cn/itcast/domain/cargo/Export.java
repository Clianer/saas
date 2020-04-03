package cn.itcast.domain.cargo;

import cn.itcast.domain.BaseEntity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Export extends BaseEntity implements Serializable{
	private static final long serialVersionUID = 1L;

	private String id;
	private Date inputDate;				//制单日期
	private String contractIds;			//打断字段，报运相关的多个合同的ID集合串
	private String customerContract;	//合同及确认书号
	private String lcno;				//信用证号
	private String consignee;			//收货人及地址
	private String marks;				//唛头
	private String shipmentPort;		//装船港
	private String destinationPort;		//目的港
	private String transportMode;		//船运SEA/空运AIR 运输方式
	private String priceCondition;		//FBO/CIF价格条件
	private String remark;				//备注
	private Integer boxNums;			//冗余，为委托服务，一个报运的总箱数
	private Double grossWeights;		//冗余，为委托服务，一个报运的总毛重
	private Double measurements;		//冗余，为委托服务，一个报运的总体积
	private Integer state;				//0-草稿 1-已上报 2-装箱 3-委托 4-发票 5-财务
	private Integer proNum; //货物数量
	private Integer extNum; //附件数量

	private List<ExportProduct> exportProducts;

	public List<ExportProduct> getExportProducts() {
		return exportProducts;
	}

	public void setExportProducts(List<ExportProduct> exportProducts) {
		this.exportProducts = exportProducts;
	}

	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	
	
	public Date getInputDate() {
		return this.inputDate;
	}
	public void setInputDate(Date inputDate) {
		this.inputDate = inputDate;
	}	
	
	public String getContractIds() {
		return this.contractIds;
	}
	public void setContractIds(String contractIds) {
		this.contractIds = contractIds;
	}	
	
	public String getCustomerContract() {
		return this.customerContract;
	}
	public void setCustomerContract(String customerContract) {
		this.customerContract = customerContract;
	}	
	
	public String getLcno() {
		return this.lcno;
	}
	public void setLcno(String lcno) {
		this.lcno = lcno;
	}	
	
	public String getConsignee() {
		return this.consignee;
	}
	public void setConsignee(String consignee) {
		this.consignee = consignee;
	}	
	
	public String getMarks() {
		return this.marks;
	}
	public void setMarks(String marks) {
		this.marks = marks;
	}	
	
	public String getShipmentPort() {
		return this.shipmentPort;
	}
	public void setShipmentPort(String shipmentPort) {
		this.shipmentPort = shipmentPort;
	}	
	
	public String getDestinationPort() {
		return this.destinationPort;
	}
	public void setDestinationPort(String destinationPort) {
		this.destinationPort = destinationPort;
	}	
	
	public String getTransportMode() {
		return this.transportMode;
	}
	public void setTransportMode(String transportMode) {
		this.transportMode = transportMode;
	}	
	
	public String getPriceCondition() {
		return this.priceCondition;
	}
	public void setPriceCondition(String priceCondition) {
		this.priceCondition = priceCondition;
	}	
	
	public String getRemark() {
		return this.remark;
	}
	public void setRemark(String remark) {
		this.remark = remark;
	}	
	
	public Integer getBoxNums() {
		return this.boxNums;
	}
	public void setBoxNums(Integer boxNums) {
		this.boxNums = boxNums;
	}	
	
	public Double getGrossWeights() {
		return this.grossWeights;
	}
	public void setGrossWeights(Double grossWeights) {
		this.grossWeights = grossWeights;
	}	
	
	public Double getMeasurements() {
		return this.measurements;
	}
	public void setMeasurements(Double measurements) {
		this.measurements = measurements;
	}	
	
	public Integer getState() {
		return this.state;
	}
	public void setState(Integer state) {
		this.state = state;
	}

	public Integer getProNum() {
		return proNum;
	}

	public void setProNum(Integer proNum) {
		this.proNum = proNum;
	}

	public Integer getExtNum() {
		return extNum;
	}

	public void setExtNum(Integer extNum) {
		this.extNum = extNum;
	}

	@Override
	public String toString() {
		return "Export{" +
				"id='" + id + '\'' +
				", inputDate=" + inputDate +
				", contractIds='" + contractIds + '\'' +
				", customerContract='" + customerContract + '\'' +
				", lcno='" + lcno + '\'' +
				", consignee='" + consignee + '\'' +
				", marks='" + marks + '\'' +
				", shipmentPort='" + shipmentPort + '\'' +
				", destinationPort='" + destinationPort + '\'' +
				", transportMode='" + transportMode + '\'' +
				", priceCondition='" + priceCondition + '\'' +
				", remark='" + remark + '\'' +
				", boxNums=" + boxNums +
				", grossWeights=" + grossWeights +
				", measurements=" + measurements +
				", state=" + state +
				'}';
	}
}
