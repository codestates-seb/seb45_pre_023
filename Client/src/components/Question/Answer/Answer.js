import FilteringButton from '../../Main/FilteringButton';
import YourAnswer from '../Answer/YourAnswer';
import { useSelector } from 'react-redux';
import AnswerList from './AnswerList';

export default function Answer() {

  const answerObjectData = useSelector((state) => state.detail.answer);
  const answerSize = useSelector((state) => state.detail.answerSize);
  const answerData = Object.values(answerObjectData);

  return (
    <>
      {/* Answer */}
      <div className="w-full mt-20 border-t-2">
        {/* 답변 최상단 */}
        <div className="w-full flex justify-between my-8">
          <h1 className="text-3xl">{answerSize.totalSize} Answer</h1>
          {}
          <FilteringButton showViews={false} />
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
