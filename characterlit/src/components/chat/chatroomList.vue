<template>
	<div id="chatroomListBox">
		<!-- List-넘겨받은 bidid:{{ givenBidId }} -->
		<div>
			<!-- <h1> login 한 user Number 테스트{{ store.getLoginUser.userNumber }}</h1> -->
			<!-- <h1>{{store.getLoginUser.userNumber}}번 사용자의 채팅방 목록</h1> -->

			<ul style="list-style: none; padding-inline-start: 0px">
				<li v-for="item in chatroomList" :key="item">
					<div
						:class="{ 'roomCard': true, 'selected': selectedItem === item }"
						@click="selectChatroom(item)"
					>
						<div id="Thumbnail">
							<img class="bidThumbnail" :src="item.thumbnail" alt="Thumbnail" />
						</div>
						<div class="cardInfo">
							<span class="cardName">
								<h3 class="itemTitle">{{ item.title }}</h3>
								<p class="itemNickName">{{ item.nickname }}</p>
							</span>
						</div>
						<!-- <p>구매자: {{ item.buyerId }}</p> -->
					</div>
				</li>
			</ul>
		</div>
	</div>
	<div>
		<chatConversation
			v-bind:givenChatroomId="chatroomId"
			v-bind:givenBidId="bidId"
			v-bind:givenUserNumber="store.getLoginUser.userNumber"
			v-bind:givenBidTitle="bidTitle"
			class="component"
		/>
	</div>
</template>

<script>
import chatConversation from './chatConversation.vue';
import { useUsersStore } from '@/stores/users';

const API_URL = 'http://localhost:8080/api/chatroomlist';

export default {
	props: ['givenBidId'],
	components: {
		chatConversation,
	},
	mounted() {
		// 넘겨받은 bid id가 0이 아니라면 채팅방 생성 후 채팅방 불러오기
		console.log('chatroomList-bidId:' + this.givenBidId);
		console.log('로그인중인 user number:' + this.store.getLoginUser.userNumber);

		// this.userNumber=this.store.getLoginUser.userNumber;

		if (this.givenBidId != 0 && this.store.getLoginUser.userNumber) {
			this.createChatrooms(this.givenBidId, this.store.getLoginUser.userNumber);
		} else {
			this.getChatrooms();
		}
	},
	data() {
		return {
			chatroomList: [],
			chatroomId: null,
			bidId: null,
			bidTitle: null,
			store: useUsersStore(),
			selectedItem: null,
		};
	},

	methods: {
		getChatrooms() {
			// n번 user의 채팅방 목록 불러오기
			fetch(API_URL, {
				method: 'GET',
				headers: {
					'access_token': localStorage.getItem('access-token'),
					'Content-Type': 'application/json',
				},
				credentials: 'include',
			})
				.then((response) => {
					if (response.ok) {
						return response.json();
					}
					throw new Error('Network response was not ok.');
				})
				.then((data) => {
					// chatroom array
					for (var i = 0; i < data.length; i++) {
						console.log('s3:' + data[i].item.thumbnail);
						this.chatroomList.push({
							chatroomId: data[i].chatroomId,
							bidId: data[i].item.bidId,
							buyerId: data[i].user.userNumber, // 구매자
							sellerId: data[i].item.userNumber, // 판매자
							thumbnail: data[i].item.thumbnail,
							title: data[i].item.title,
							nickname: data[i].item.nickname,
						});
					}
				})
				.catch((error) => {
					console.error('에러가 났어요.', error);
				});
		},
		selectChatroom(item) {
			console.log('item:' + item.chatroomId);
			this.chatroomId = item.chatroomId;
			this.bidId = item.bidId;
			this.bidTitle = item.title;
			this.selectedItem = item;
		},
		createChatrooms(bidId, buyerId) {
			fetch('http://localhost:8080/api/chatroom/create', {
				method: 'POST',
				headers: {
					'Content-Type': 'application/json',
				},
				body: JSON.stringify({
					bidId: bidId,
					buyerId: buyerId,
				}),
			})
				.then((response) => {
					if (response.ok) {
						console.log(
							buyerId + '번 user에 대한 ' + bidId + '번 물품 문의방 생성 완료.',
						);

						this.getChatrooms(); // 채팅방 생성 후 불러오기
					} else {
						if (response.status === 400) {
							console.log('이미 존재하는 채팅방입니다.');
							window.location.href = 'http://localhost:5173/chatPage';
						}
						throw new Error('Network response was not ok.');
					}
				})
				.catch((error) => {
					console.error('채팅방 생성 프로세스에서 문제가 생겼어요.', error);
				});
		},
	},
};
</script>
<style scopped>
.itemNickName {
	margin: 10px 0;
	font-size: small;
}
.itemTitle {
	margin: 0;
}

#Thumbnail {
	float: left;
}
.bidThumbnail {
	border-radius: 50px;
	width: 80px;
	height: 80px;
	margin-right: 20px;
}
.cardName {
	font-weight: bold;
	font-size: 18px;
	margin-bottom: 10px;
}
.cardBid {
	margin-top: 10px;
}

.chatComponent {
	display: flex;
	flex-direction: row;
}
#chatroomListBox {
	width: 800px; /* 너비를 800px로 조정 */
	border-right: 1px solid #ddd;
	overflow-y: auto;
	height: 500px;
}
.roomCard {
	padding: 20px;
	height: 100px; /* 높이를 약간 늘림 */
	border-bottom: 1px solid #eee; /* 색상을 더 부드러운 회색으로 변경 */
	transition: background-color 0.3s ease;
	cursor: pointer;
	display: flex;
	align-items: center;
	margin-bottom: 10px;
}
.roomCard:hover,
.roomCard.selected {
	background-color: #f0f0f0; /* 호버 배경색 변경 */
}
#chatroomListBox {
	height: 500px;
	overflow-y: auto;
	border-right: 1px solid #ddd;
}

.roomCard {
	display: flex;
	align-items: center;
	margin-bottom: 10px;
	transition: background-color 0.3s ease;
	cursor: pointer;
}
</style>
