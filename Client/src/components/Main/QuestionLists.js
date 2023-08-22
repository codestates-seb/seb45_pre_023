import Paging from './Pagination/Paging';
import FilteringButton from './FilteringButton';
import axios from 'axios';
import { useEffect } from 'react';
import QuestionData from './QuestionData';
import { useDispatch, useSelector } from 'react-redux';
import { setTagList } from '../../redux/createSlice/AskSlice';
import { pageInfo, setQuestions } from '../../redux/createSlice/QuestionSlice';
import { Link } from 'react-router-dom';

export default function QuestionList() {

  const getTags = () => {
    return axios
      .get('http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/tags')
      .then((res) => dispatch(setTagList(res.data.data)))
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    getTags();
  }, []);

  const questionData = useSelector((state) => state.questions.value);

  const pageInfos = useSelector((state) => state.questions.pageInfo.totalSize);
  const queryParams = useSelector((state) => state.questions.queryParams);
  const queryString = new URLSearchParams(queryParams).toString();

  const dispatch = useDispatch();


  useEffect(() => {
    axios
      .get(
        `http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions?${queryString}`
      )
      .then((res) => {
        dispatch(setQuestions(res.data.data));
        dispatch(pageInfo(res.data.pageInfo));
        console.log(res.data.data);
      })
      .catch((err) => {
        console.log(err);
      });
  }, [queryString]);

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
          <h1 className="text-3xl">{pageInfos} Questions</h1>
              <FilteringButton/>
        </div>

        {questionData.map((el, index) => (
          <QuestionData key={index} el={el} />
        ))}
        <Paging />
      </div>
    </>
  );
}

