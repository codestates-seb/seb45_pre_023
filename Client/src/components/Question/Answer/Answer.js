import FilteringButton from '../../Main/FilteringButton';
import YourAnswer from '../Answer/YourAnswer';
import { useSelector } from 'react-redux';
// import { useEffect } from 'react';
// import axios from 'axios';
// import { useDispatch } from 'react-redux';
// import { answerAdd } from '../../../redux/createSlice/QuestionDetailSlice';
import AnswerList from './AnswerList';

export default function Answer() {

  const answerObjectData = useSelector((state) => state.detail.answer);
  console.log(answerObjectData);
  const answerData = Object.values(answerObjectData);

  return (
    <>
      {/* Answer */}
      <div className="w-full mt-20 border-t-2">
        {/* 답변 최상단 */}
        <div className="w-full flex justify-between my-8">
          <h1 className="text-3xl">1 Answer</h1>
          <FilteringButton />
        </div>

        {/* 답변 내용 */}
        <ul className='w-full flex flex-col'>
          {answerData.map((el, index) => (
            <AnswerList el={el} index={index} key={index} />
          ))}
        </ul>
      </div>
      <YourAnswer />
    </>
  );
}
