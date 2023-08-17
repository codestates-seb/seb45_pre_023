export default function getTimeDifference(timeString) {
    const currentTime = new Date(); // 현재 시간
    const targetTime = new Date(timeString); // 입력된 시간
    const timeDifference = currentTime - targetTime; // 시간 차이 계산 (밀리초 단위)
  
    const hoursAgo = Math.floor(timeDifference / (1000 * 60 * 60)); // 시간으로 변환
  
    return hoursAgo;
  }