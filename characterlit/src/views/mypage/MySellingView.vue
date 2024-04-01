<template>
	<div class="selling-list-container">
		<h2>나의 판매 내역</h2>
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
				<p>시작 가격: {{ item.startBid }} 원</p>
				<P>시작 시간: {{ item.endDate }}</P>
				<p>종료 시간: {{ item.endDate }}</p>
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

const fetchSellingList = async () => {
	try {
		const response = await fetch(`http://localhost:8080/api/bid/mysell`, {
			method: 'GET',
			headers: {
				'Content-Type': 'application/json',
				'access_token': localStorage.getItem('access-token'),
			},
			credentials: 'include',
		});
		const data = await response.json();
		sellingList.value = data;
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
	align-items: center; /* 이미지를 포함한 모든 항목을 중앙으로 정렬 */
	justify-content: flex-start;
	gap: 16px; /* 썸네일과 세부 정보 사이의 간격 */
	padding: 16px;
	border: 1px solid #e5e7eb;
	border-radius: 8px;
	margin-bottom: 0px;
}

.item-thumbnail img {
	width: 150px;
	height: 150px;
	object-fit: cover;
	border: 1px solid #e5e7eb; /* Light gray border for the image */
	box-shadow: 0px 4px 4px 0px rgb(63, 62, 62);
	border-radius: 10px;
}

.item-details {
	display: grid;
	flex-grow: 1;
	gap: 6px;
}

.item-details h2 {
	font-size: 20px; /* Large text size */
	font-weight: 600; /* Semi-bold */
	margin: 0 0 15px 0;
	word-break: break-word;
	white-space: nowrap;
	overflow: hidden;
	text-overflow: ellipsis;
	max-width: 200px; /* Adjust the width as needed */
}

.item-details p {
	font-size: 0.875rem; /* Small text size */
	line-height: 1; /* No additional line height */
	margin: 5px 0;
}

/* Adding a border between items for visual separation */
.bidding-list-container > .bid-item:not(:last-child) {
	border-bottom: 1px solid #e5e7eb;
}

/* Styling for the 'Present Price' and 'My Bid Price' to differentiate */
.presentBid,
.requestBid {
	font-weight: 600;
	color: #007bff; /* A shade of blue for emphasis */
}

/* Optional: Adding hover effect for interaction feedback */
.bid-item:hover {
	background-color: #f9fafb; /* Very light gray background on hover */
	cursor: pointer; /* Change cursor to indicate clickable */
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
