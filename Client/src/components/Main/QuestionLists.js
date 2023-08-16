import Paging from './Pagination/Paging';
import FilteringButton from './FilteringButton';
import axios from 'axios';
import { useEffect, useState } from 'react';
import QuestionData from './QuestionData';

export default function QuestionList() {
  const [isData, setIsData] = useState([]);

  // 엔드포인트로 페이지 번호 보내는데,, 이걸 어떻게 보내지?
  // /page=${isPage}

  useEffect(() => {
    axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/questions`
      )
      .then((res) => {
        setIsData(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  return (
    <>
      <div className="w-[1000px] flex flex-col justify-center border-l-2">
        {/* 최상단 ask 버튼 */}
        <div className="w-full flex justify-between my-6 ml-6">
          <h1 className="text-3xl">ALL Questions</h1>
          <button className="w-[150px] h-[50px] text-[white] rounded-2xl bg-[#2196F3] ">
            Asked Question
          </button>
        </div>

        <div className="w-full flex justify-between my-6 ml-6">
          <h1 className="text-3xl">0 Questions</h1>
          <FilteringButton />
        </div>

        {isData.map((el, index) => (
          <QuestionData
            key={index}
            el={el}
          />
        ))}
        <Paging setIsData={setIsData} />
      </div>
    </>
  );
}
