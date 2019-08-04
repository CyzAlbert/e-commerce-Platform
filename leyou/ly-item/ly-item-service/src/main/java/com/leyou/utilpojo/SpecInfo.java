package com.leyou.utilpojo;

import java.util.Arrays;
import java.util.List;

public class SpecInfo {
    private String group;

    public static class Params{
        private String k;
        private boolean searchable;
        private boolean global;
        private List<String> options;
        private String unit;
        private boolean numerical;

        public String getK() {
            return k;
        }

        public void setK(String k) {
            this.k = k;
        }

        public boolean isSearchable() {
            return searchable;
        }

        public void setSearchable(boolean searchable) {
            this.searchable = searchable;
        }

        public boolean isGlobal() {
            return global;
        }

        public void setGlobal(boolean global) {
            this.global = global;
        }

        public List<String> getOptions() {
            return options;
        }

        public void setOptions(List<String> options) {
            this.options = options;
        }

        public String getUnit() {
            return unit;
        }

        public void setUnit(String unit) {
            this.unit = unit;
        }

        public boolean isNumerical() {
            return numerical;
        }

        public void setNumerical(boolean numerical) {
            this.numerical = numerical;
        }

    }

    private List<Params> params= Arrays.asList(new Params());

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public List<Params> getParams() {
        return params;
    }

    public void setParams(List<Params> params) {
        this.params = params;
    }
}
