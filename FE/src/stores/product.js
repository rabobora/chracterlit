import { ref, computed } from "vue";
import { defineStore } from "pinia";
import router from "@/router";
import axios from "axios";

export const useProductStore = defineStore("product", () => {
    ////// STATE//////////////////////////////////////////////

    const allProductList = ref([]);
    const searchProductList = ref([])
    const searchCategoryList = ref([])
    const productDetail = ref({})
    const top3ProductList = ref({})


    ////// GETTER/////////////////////////////////////////////

    const getAllProductList = computed(() => {
        return allProductList.value
    })

    const getSearchProductList = computed(() => {
        return searchProductList.value
    })

    const getCategoryList = computed(() => {
        return searchCategoryList.value
    })
    const getProductDetail = computed(() => {
        return productDetail.value
    })
    const gettop3ProductList = computed(()=> {
        return top3ProductList.value
    })


    ////// ACTION/////////////////////////////////////////////

    // 모든 상품 정보 가져오기
    const researchAllProduct = function () {
        axios({
          url: `${import.meta.env.VITE_REST_API}/bid/search/all`,
          method: "GET",
          withCredentials:true
        })
          .then((res) => {
            allProductList.value = res.data
            console.log("요청 성공")
          })
          .catch((err) => {
            console.error("요청 실패:", err)
            
          })
      }
    
      // 검색된 상품의 정보 가져오기
    const researchSearchResult = async (word) => {
        try {
            const res = await axios.get(`${import.meta.env.VITE_REST_API}/bid/search/${word}`,{withCredentials:true})
            searchProductList.value = res.data
            
            return res.data.length > 0
        } catch (err) {            
            return false
        }
    }

    // 카테고리 별 필터링 후 출력
    const researchCategory = function (categoryid) {
        axios({
            url: `${import.meta.env.VITE_REST_API}/bid/search/category/${categoryid}`,
            method: "GET",
            withCredentials:true
        })
        .then((res) => {
            searchCategoryList.value = res.data;
        })
        .catch((err) => {
            console.error("카테고리 조회 실패", err);
        });
    };
    
    // 상품 상세정보 글 보기
    const researchProductDetail = async (bidid) => {
        try {
            const response = await axios.get(`${import.meta.env.VITE_REST_API}/bid/read/${bidid}`, {withCredentials:true});
            if (response.data) {
                return response.data; 
            } else {
                console.error('No product details returned from the server for id:', bidid);
                return null; 
            }
        } catch (err) {
            console.error('Error fetching product details for id:', bidid, err);
            return null; 
        }
    };

    const createProduct = function (product) {
        
        const accessToken = localStorage.getItem('access-token');    
    
        if (!accessToken) {
            console.error('Access token is not available.');
            return; 
        }
        
        axios({
            url: `${import.meta.env.VITE_REST_API}/bid/create`,
            method: "POST",
            headers: {
                'access_token': accessToken 
            },
            data: product,
            withCredentials:true
        })
        .then((res) => {
            window.location.reload()
        })
        .catch((err) => {
            console.error(err);
            // 오류 처리 로직
        });
    };

    

    // 상품 글 수정
    const updateProduct = function (productData, bidid) {
        const accessToken = localStorage.getItem('access-token');
        
        if (!accessToken) {
            console.error('Access token is not available.');
            return;
        }

        const { nickname, ...product } = productData;
        
        axios({
            url: `${import.meta.env.VITE_REST_API}/bid/modify/${bidid}`,
            method: "PUT",
            headers: {
                'access_token': accessToken
            },
            data: product,
            withCredentials: true
        })
        .then((res) => {
            alert("상품 정보가 성공적으로 수정되었습니다.");
            router.push(`/product/${bidid}`);
            
        })
        .catch((err) => {
            console.error(err);
            alert("상품 정보 수정에 실패했습니다.");
            router.push(`/product/${bidid}`);
            

        });
    };

    


    // 상품 글 삭제
    const deleteProduct = async function (bidid) {
        await axios({
          url: `${import.meta.env.VITE_REST_API}/bid/delete/${bidid}`,
          method: "DELETE",
          withCredentials: true
        })
          .then((res) => {              
              alert("상품 정보가 성공적으로 삭제되었습니다.")
          })
          .catch((err) => {
                              
              alert(err.response.data.message || "상품 정보 삭제에 실패했습니다.")
          })
          
          router.push('/product/list');
          
    }
    


    // 이미지 업로드
    const s3ImageUpload = async (images) => {
        let formData = new FormData();
        for (let i = 0; i < images.length; i++) {
            formData.append("image", images[i])
        }
    
        try {
            const response = await axios.post(`${import.meta.env.VITE_REST_API}/s3/upload`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                     
                },
                withCredentials:true
            });
            return response.data // 이미지 URL 목록 반환
        } catch (err) {
            console.error("요청 실패:", err)
            return []
        }
    }


    // 썸네일 업로드
    const s3ThumbnailUpload = async (thumbnail) => {
        let formData = new FormData();
        formData.append("thumbnail", thumbnail)
    
        try {
            const response = await axios.post(`${import.meta.env.VITE_REST_API}/lambda/upload`, formData, {
                headers: {
                    "Content-Type": "multipart/form-data",
                },
                withCredentials:true
            });
            return response.data // 썸네일 이미지 URL 반환
        } catch (err) {
            console.error("요청 실패", err)
            return ""
        }
    }

    // 모든 상품 정보 가져오기
    const researchTop3Product = function () {
        axios({
            url: `${import.meta.env.VITE_REST_API}/bid/search/top3`,
            method: "GET",
            withCredentials:true
        })
            .then((res) => {
            top3ProductList.value = res.data
            console.log("요청 성공")
            })
            .catch((err) => {
            console.error("요청 실패:", err)
            
            })
        }


    const sortState = ref({
        sortField: 'bid_index',
        sortOrder: 'desc' 
    });
    
    const getSortedProductList = computed(() => {
        // 배열 여부를 확인하고, 배열이 아니면 빈 배열을 사용
        const selectedList = Array.isArray(getSearchProductList.value) && getSearchProductList.value.length > 0 
            ? getSearchProductList.value
            : (Array.isArray(getCategoryList.value) && getCategoryList.value.length > 0 
                ? getCategoryList.value
                : Array.isArray(getAllProductList.value) ? getAllProductList.value : []);    
     
        selectedList.forEach(product => {
            product.auctionStatusText = getAuctionStatusText(product.bid_status);
        });
        
        return selectedList.sort((a, b) => {
            if (sortState.value.sortOrder === 'desc') {
                return b[sortState.value.sortField] - a[sortState.value.sortField];
            }
            return a[sortState.value.sortField] - b[sortState.value.sortField];
        });
    });
    
    function getAuctionStatusText(bidStatus) {
        switch (bidStatus) {
        case 1: return '경매 중';
        case 2: return '경매 종료';
        default: return '경매 전';
        }
    } 
    
    const setSortOrder = (field, order) => {
        sortState.value.sortField = field;
        sortState.value.sortOrder = order;
      };

    const filterByBidStatus = (status) => {
    
    if (status !== null) {
        sortState.value.filterStatus = status; 
    } else {
        sortState.value.filterStatus = null;
    }
    };

           

    return {
        setSortOrder,
        allProductList,
        searchProductList,
        searchCategoryList,
        productDetail,
        getAllProductList,
        getSearchProductList,
        getCategoryList,
        getProductDetail,
        sortState,
        getSortedProductList,
        gettop3ProductList,
        top3ProductList,
        getAuctionStatusText,
        researchAllProduct,
        researchSearchResult,
        researchCategory,
        researchProductDetail,
        createProduct,
        updateProduct,
        deleteProduct,
        s3ImageUpload,
        s3ThumbnailUpload,
        filterByBidStatus,  
        researchTop3Product,      

    }
}, { persist: true })