import Paging from './Pagination/Paging';
import FilteringButton from './FilteringButton';
import axios from 'axios';
import { useEffect, useState } from 'react';
import QuestionData from './QuestionData';
import { useDispatch, useSelector } from 'react-redux';
import { setQuestions } from '../../redux/createSlice/QuestionSlice';
import { Link } from 'react-router-dom';

import { useNavigate } from 'react-router-dom';
import { RouteConst } from '../../Interface/RouteConst';

export default function QuestionList() {
  const [isData, setIsData] = useState([]);

  const dispatch = useDispatch();


  useEffect(() => {
    axios
      .get(
        `http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/questions`
      )
      .then((res) => {
        dispatch(setQuestions(res.data.data));
        console.log(res);
      })
      .catch((err) => {
        console.log(err);
      });
  }, []);

  const questionData = useSelector((state) => state.questions.value);
  console.log(questionData);

  return (
    <>
      <div className="w-[1000px] mt-12 flex flex-col justify-center border-l-2">
        {/* 최상단 ask 버튼 */}
        <div className="w-full flex justify-between my-6 ml-6">
          <h1 className="text-3xl">ALL Questions</h1>
          <Link to={`/questions/add`}>
            <button className="w-[150px] h-[50px] text-[white] rounded-2xl bg-[#2196F3] ">
              Asked Question
            </button>
          </Link>

        </div>

        <div className="w-full flex justify-between my-6 ml-6">
          <h1 className="text-3xl">0 Questions</h1>
          <FilteringButton />
        </div>

        {/* {isData.map((el, index) => ( */}
        <QuestionData
        // key={index}
        // el={el}
        />
        {/* ))} */}
        <Paging setIsData={setIsData} />
      </div>
    </>
  );
}
