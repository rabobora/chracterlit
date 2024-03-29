<template>
    <header>헤더가 들어갈 자리</header>

    <div class="searchandword">
        <div class="fadecomponents">        
            <div class="catchpri"> 
                <h1>세상에 있는 모든 굿즈,<br>캐릭터릿</h1>
            </div>
            <div class="searchbar">
                <SearchBarView />
            </div>
            <div class="gotoitemlist">
                <p @click="gotoproductlist" class="productlist">시작하기</p>
            </div>    
        </div>        
    </div>
    <!-- <div>다음페이지</div>     -->
<div>	<div>
		<h1>Login</h1>
		<button @click="onLogin">LogIn</button>
		<button @click="onLogout">LogOut</button>
		<button @click="reissueToken">ReissueAccessToken</button>
		<button @click="getMy">(User 권한 필요 O)</button>
		<button @click="getMain">(User 권한 필요 X)</button>
		<button @click="getLoginUser">로그인한 유저 정보 요청</button>
		<button @click="getlist">게시글 호출</button>
	</div>
</div>







</template>

<script setup>
  import { ref, onMounted,computed } from 'vue';
  import { useProductStore } from '../../stores/product';
  import SearchBarView from '../product/SearchBarView.vue'
  
  import { useRoute,useRouter } from 'vue-router';
  const router = useRouter();
  const route = useRoute();
  const productStore = useProductStore();

  const gotoproductlist = () => {
    router.push({ name: 'productList'})
  }


  const getCookie = (name) => {
	let matches = document.cookie.match(
		new RegExp(
			'(?:^|; )' +
				name.replace(/([\.$?*|{}\(\)\[\]\\\/\+^])/g, '\\$1') +
				'=([^;]*)',
		),
	);
	return matches ? decodeURIComponent(matches[1]) : undefined;
};

const saveTokenToLcalStorage = () => {
	if (
		getCookie('access_token') != null &&
		getCookie('access_token') !== 'undefined'
	)
		localStorage.setItem('access-token', getCookie('access_token'));
};

const onLogin = () => {
	window.location.href = 'http://localhost:8080/oauth2/authorization/naver';
};

const onLogout = () => {
	fetch('http://localhost:8080/logout', {
		method: 'POST',
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const reissueToken = () => {
	saveTokenToLcalStorage();
	fetch('http://localhost:8080/reissue', {
		method: 'POST',
		headers: { 'access_token': localStorage.getItem('access-token') },
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const getMy = () => {
	saveTokenToLcalStorage();
	fetch('http://localhost:8080/my', {
		method: 'GET',
		headers: { 'access_token': localStorage.getItem('access-token') },
		credentials: 'include',
	})
		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};

const getlist = () => {

	fetch('http://localhost:8080/api/bid/search/all"', {
		method: 'GET'})

		.then((res) => console.log(res))
		.catch((error) => console.log(error));
};


const getMain = () => {
	// saveTokenToLcalStorage();
	fetch('http://localhost:8080/', {
		method: 'GET',
		credentials: 'include',
	}).then((res) => {
		console.log(res);
	});
};

const getLoginUser = () => {
	saveTokenToLcalStorage();
	fetch('http://localhost:8080/api/users/id', {
		method: 'GET',
		headers: {
			'access_token': localStorage.getItem('access-token'),
			'Content-type': 'application/json',
		},
		credentials: 'include',
	})
		.then((res) => console.log(JSON.stringify(res.data)))
		.catch((error) => console.log(error));
};






</script>

<style scoped>
.fadecomponents {

}

@keyframes fade-in {
  from {
    opacity: 0; /* 시작 시 요소가 완전히 투명 */
  }
  to {
    opacity: 1; /* 종료 시 요소가 완전히 불투명 */
  }
}

.fadecomponents > div {
    animation: fade-in 2.5s ease-out 0.5s; /* 애니메이션 이름, 지속 시간, 타이밍 함수, 시작 지연 */
    animation-fill-mode: forwards; /* 애니메이션 종료 시 요소가 최종 상태를 유지하도록 설정 */
}

.productlist {
    display: flex; 
    align-items: center; 
    justify-content: center;
    padding: 10px 20px; 
    background-color: black; 
    border: 2px solid #dcdcdc; 
    border-radius: 5px; 
    cursor: pointer; 
    text-decoration: none; 
    color: white;     
    transition: background-color 0.3s, color 0.3s;
    height: 50px;
    width: 100px;
    font-size: x-large;
    font-weight: bold;
    margin-left: 38%;
}

.productlist:hover {
    background-color: #e2dddd; 
    color: #3e78e5; 
}

.searchandword {
    background-image: url('../../pictures/깃털.png'); 
    background-size: auto;
    background-repeat: no-repeat;
    display: flex;
    flex-direction: column;
    justify-content: flex-start;
    align-items: center; 
    height: 100vh;
    padding-top: 7%; 
}

.catchpri{
    margin-left: -2%;
    width: 110%;
    text-align: center;
    font-size: x-large;
}
.searchbar {
    display: flex;
    align-items: center; 
    margin-top: 1%; 
    text-align: center;
    width: 500px;
    margin-left: 3%;
    justify-content: center; 
}
.gotoitemlist{
    margin-top: 10%;
    text-align: center;
}
.pictureandwords{
    
}
</style>