<template>
	<div class="selling-list-container">
		<h2>나의 판매 내역</h2>
		<h1 v-if="sellingList.length===0" style="color: rgb(63, 62, 62);">판매한 상품이 없습니다</h1>
		<div
			v-for="item in sellingList"
			:key="item.bidId"
			class="bid-item"
			@click="navigateToItem(item.bidId)"
		>
			<div class="item-thumbnail">
				<img
					:src="item.thumbnail || defaultImage"
					alt="Item Thumbnail"
				/>
			</div>
			<div class="item-details">
				<h2>{{ item.title }}</h2>
				<div>
					<p v-if="item.bidStatus !== 2">시작 가격: {{ item.startBid }} 원</p>
					<p v-else-if="item.bidStatus === 2 && item.finalBid !== null"> 낙찰 가격: {{ item.finalBid }} 원</p>
					<p v-else-if="item.bidStatus === 2 && item.finalBid === null"> 유찰되었습니다</p>
				</div>
				<P>시작 시간: {{ formatDateTime(item.startDate) }}</P>
				<p>종료 시간: {{ formatDateTime(item.endDate) }}</p>
				<p>조회 수: {{ item.viewCount }}</p>
			</div>
			<div class="status-box">
				<div
					class="item-status"
					:class="{
						'pre-auction': item.bidStatus === 0,
						'in-auction': item.bidStatus === 1,
						'post-auction': item.bidStatus === 2,
					}"
				>
					<span v-if="item.bidStatus === 0">경매 이전</span>
					<span v-else-if="item.bidStatus === 1">경매 진행</span>
					<span v-else-if="item.bidStatus === 2">경매 종료</span>
				</div>
				<div
					class="item-status"
					:class="{
						'hidden-element': item.bidStatus !== 2,
						'pre-auction': !item.isPaid,
						'post-auction': item.isPaid,
					}"
				>
					<span v-if="item.winnerNumber === null">경매 유찰</span>
					<span v-else-if="!item.isPaid">결제 대기</span>
					<span v-else-if="item.isPaid">결제 완료</span>
				</div>
			</div>
		</div>
	</div>
</template>

<script setup>
import { ref, onMounted } from 'vue';
import { useRouter } from 'vue-router';
import defaultImage from '@/assets/default_image.png';
const router = useRouter();

const sellingList = ref([]);

const navigateToItem = (bidId) => {
	router.push({ name: 'ReadView', params: { number: bidId } });
};

function formatDateTime(isoString) {
        const date = new Date(isoString);
        const year = date.getFullYear();
        const month = (date.getMonth() + 1).toString().padStart(2, '0');
        const day = date.getDate().toString().padStart(2, '0');
        const hour = date.getHours().toString().padStart(2, '0');
        const minute = date.getMinutes().toString().padStart(2, '0');

        return `${year}년 ${month}월 ${day}일 ${hour}시 ${minute}분`;
      }

const fetchSellingList = async () => {
	try {
		const response = await fetch(`http://j10b105.p.ssafy.io:8080/api/bid/mysell`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				'access_token': localStorage.getItem('access-token'),
			},
			credentials: 'include',
		});
		const data = await response.json();
		sellingList.value = data.sort((a, b) => b.bidId - a.bidId);
		console.log('My Bid List fetched successfully', data);
	} catch (error) {
		console.error('Failed to fetch my bid list:', error);
	}
};

onMounted(async () => {
	await fetchSellingList();
});
</script>

<style>
.selling-list-container {
	display: flex;
	flex-direction: column;
	max-width: 800px;
	margin: auto;
	padding: 20px;
	gap: 20px;
}

.bid-item {
	display: flex;
	align-items: center;
	justify-content: flex-start;
	gap: 16px; 
	padding: 16px;
	border: 1px solid #e5e7eb;
	border-radius: 8px;
	margin-bottom: 0px;
}

.item-thumbnail img {
	width: 150px;
	height: 150px;
	object-fit: cover;
	border: 1px solid #e5e7eb;
	box-shadow: 0px 4px 4px 0px rgb(63, 62, 62);
	border-radius: 10px;
}

.item-details {
	display: grid;
	flex-grow: 1;
	gap: 6px;
}

.item-details h2 {
	font-size: 20px; 
	font-weight: 600; 
	margin: 0 0 15px 0;
	word-break: break-word;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	max-width: 200px; 
}

.item-details p {
	font-size: 0.875rem; 
	line-height: 1; 
	margin: 5px 0;
}

.bidding-list-container > .bid-item:not(:last-child) {
	border-bottom: 1px solid #e5e7eb;
}

.presentBid,
.requestBid {
	font-weight: 600;
	color: #007bff;
}

.bid-item:hover {
	background-color: #f9fafb;
	cursor: pointer; 
}

.status-box .item-status {
	margin-bottom: 10px;
}

.status-box .item-status:last-child {
	margin-bottom: 0;
}

.item-status {
	flex: none;
	padding: 24px;
	border-radius: 4px;
	color: white;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	max-width: 100px;
}

.pre-auction {
	background-color: #b4b4b4;
}

.in-auction {
	background-color: #ff4500;
	animation: burning 1s infinite; /* 불타는 애니메이션 */
}

@keyframes burning {
	0% {
		opacity: 1;
	}
	50% {
		opacity: 0.5;
	}
	100% {
		opacity: 1;
	}
}

.post-auction {
	background-color: #000000;
}

.hidden-element {
	visibility: hidden;
}
</style>
