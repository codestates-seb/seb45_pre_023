import axios from "axios";


export default function FilteringButton() {

  // 최신순
  const handleNewest = () => {
    return axios.get("http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions?page=1&sort=CREATED_DATE&tag=tag1")
    .then((res) => {
      console.log(res);
    })
    .catch((err) => {
      console.log(err);
    })
  }

  // 조회순
  const handleActive = () => {
    return axios.get("http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions?page=1&sort=VIEWS&tag=tag1")
    .then((res) => {
      console.log(res);
    })
    .catch((err) => {
      console.log(err);
    })
  }

  // 추천순
  const handleRecommend = () => {
    return axios.get("http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions?page=1&RECOMMEND&tag=tag1")
    .then((res) => {
      console.log(res);
    })
    .catch((err) => {
      console.log(err);
    })
  }


  return (
    <>
        <div className = "w-50 flex justify-around rounded-2xl border border-[#E0E0E0]">
          <button onClick={handleNewest} className= "w-[70px] h-[40px]  border-l rounded-2xl border-[#E0E0E0] hover:bg-[#bdbdbd]">Newest</button>
          <button onClick={handleActive} className= "w-[70px] h-[40px]  border-l border-[#E0E0E0] hover:bg-[#bdbdbd]">Active</button>
          <button onClick={handleRecommend} className= "w-[70px] h-[40px] border-l border-r border-[#E0E0E0] hover:bg-[#bdbdbd]">Bountied</button>
          <button className= "w-[100px] h-[40px] border-r rounded-2xl border-[#E0E0E0] hover:bg-[#bdbdbd]">Unanswered</button>
        </div>
    </>
  );
}
