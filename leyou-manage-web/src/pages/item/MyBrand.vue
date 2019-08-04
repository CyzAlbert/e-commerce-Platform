<template>
  <v-card>
    <v-card-title flat color="white">
      <v-btn color="primary" @click="addBrand">新增品牌</v-btn>
      <!--空间隔离组件-->
      <v-spacer />
      <!--搜索框，与search属性关联-->
      <v-text-field label="输入关键字搜索" v-model="search" append-icon="search" hide-details/>
    </v-card-title>
    <v-data-table
      :headers="headers"
      :items="brands"
      :pagination.sync="pagination"
      :total-items="totalBrands"
      :loading="loading"
      class="elevation-1"
    >
      <template slot="items" slot-scope="props">
        <td class="text-xs-center">{{ props.item.id }}</td>
        <td class="text-xs-center">{{ props.item.name }}</td>
        <td class="text-xs-center"><img v-if="props.item.image" :src="props.item.image" width="130" height="40"/></td>
        <td class="text-xs-center">{{ props.item.letter }}</td>
        <td class="text-xs-center">
          <v-btn small class="mr-2" @click="editBrand(props.item)">
            编辑
          </v-btn>
          <v-btn small @click="deleteBrand(props.item)">
            delete
          </v-btn>
        </td>
        <!--弹出的对话框-->
        <v-dialog max-width="500" v-model="show" persistent>
          <v-card>
            <!--对话框的标题-->
            <v-toolbar dense dark color="primary">
              <v-toolbar-title>新增品牌</v-toolbar-title>
              <v-spacer/>
              <!--关闭窗口的按钮-->
              <v-btn icon @click="closeWindow"><v-icon>close</v-icon></v-btn>
            </v-toolbar>
            <!--对话框的内容，表单-->
            <v-card-text class="px-5">
              <my-brand-form @close="closeWindow" :oldBrand="oldBrand"/>
            </v-card-text>
          </v-card>
        </v-dialog>
      </template>
    </v-data-table>
  </v-card>
</template>

<script>
  import MyBrandForm from './MyBrandForm'

  export default {
    name: "myBrand",
    data () {
      return {
        totalBrands: 0, // 总条数
        brands: [], // 当前页品牌数据
        search: "", // 查询关键字
        loading: true, // 是否在加载中
        pagination: {}, // 分页信息
        headers: [ // 头信息
          {text: 'id', align: 'center', value: 'id'},
          {text: '名称', align: 'center', value: 'name', sortable: false},
          {text: 'LOGO', align: 'center', value: 'image', sortable: false},
          {text: '首字母', align: 'center', value: 'letter'},
          {text: '操作', align: 'center', value: 'id', sortable: false }
        ],
        show:false, //控制新建对话框的显示
        oldBrand:{}
      }
    },
    watch:{
      pagination:{
        deep: true, // 深度监视
        handler(){
          this.getDataFromServer();
        }
      },
      search(){
        this.pagination.page = 1;
        this.getDataFromServer();
      }
    },
    methods: {
      getDataFromServer(){ // 从服务端加载数据的函数
        this.loading=true;
        this.$http.get("/item/brand/page",{
          params: {
            page: this.pagination.page, // 当前页
            rows: this.pagination.rowsPerPage, // 每页条数
            sortBy: this.pagination.sortBy, // 排序字段
            desc: this.pagination.descending, // 是否降序
            key: this.search, // 查询字段
          }
        }).then(resp=>{
          this.totalBrands = resp.data.total; // 总条数
          this.brands = resp.data.items; // 品牌数据
          this.loading = false; // 加载完成
        })
        // 伪造演示数据
        const brands = [
          {
            "id": 2032,
            "name": "OPPO",
            "image": "http://img10.360buyimg.com/popshop/jfs/t2119/133/2264148064/4303/b8ab3755/56b2f385N8e4eb051.jpg",
            "letter": "O",
            "categories": null
          },
          {
            "id": 2033,
            "name": "飞利浦（PHILIPS）",
            "image": "http://img12.360buyimg.com/popshop/jfs/t18361/122/1318410299/1870/36fe70c9/5ac43a4dNa44a0ce0.jpg",
            "letter": "F",
            "categories": null
          },
          {
            "id": 2034,
            "name": "华为（HUAWEI）",
            "image": "http://img10.360buyimg.com/popshop/jfs/t5662/36/8888655583/7806/1c629c01/598033b4Nd6055897.jpg",
            "letter": "H",
            "categories": null
          },
          {
            "id": 2036,
            "name": "酷派（Coolpad）",
            "image": "http://img10.360buyimg.com/popshop/jfs/t2521/347/883897149/3732/91c917ec/5670cf96Ncffa2ae6.jpg",
            "letter": "K",
            "categories": null
          },
          {
            "id": 2037,
            "name": "魅族（MEIZU）",
            "image": "http://img13.360buyimg.com/popshop/jfs/t3511/131/31887105/4943/48f83fa9/57fdf4b8N6e95624d.jpg",
            "letter": "M",
            "categories": null
          }
        ];
        // 延迟一段时间，模拟数据请求时间
        // setTimeout(()=>{
        //   this.brands = brands; // 赋值给品牌数组
        //   this.totalBrands = brands.length; // 赋值数据总条数
        //   this.loading = false; // 数据加载完成
        // }, 1000);
      },
      addBrand(){
        // 控制弹窗可见：
        this.show = true;
      },
      editBrand(oldBrand){
        // 控制弹窗可见：
        this.show = true;
        this.oldBrand = oldBrand
      },
      deleteBrand(){
        // 控制弹窗可见：
        this.show = false;
      },
      closeWindow(){
        // 关闭窗口
        this.show = false;
        // 重新加载数据
        this.getDataFromServer();
      }
    },
    // 渲染后执行
    mounted(){
      this.getDataFromServer() // 调用数据初始化函数
    },
    components:{
      MyBrandForm
    }
  }
</script>

<!-- scoped:当前样式只作用于当前组件的节点 -->
<style scoped>

</style>
