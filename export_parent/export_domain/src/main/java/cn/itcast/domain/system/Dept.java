package cn.itcast.domain.system;

/**
 * @Author han
 * @Date 2020/3/9 19:48
 * @Version 1.0
 **/
public class Dept {
    private String id;
    private String deptName;
    private Integer state;
    private String companyId;
    private String companyName;
    // 当前部门关联了一个父部门对象。 association
    private Dept parent;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDeptName() {
        return deptName;
    }

    public void setDeptName(String deptName) {
        this.deptName = deptName;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public Dept getParent() {
        return parent;
    }

    public void setParent(Dept parent) {
        this.parent = parent;
    }
}
