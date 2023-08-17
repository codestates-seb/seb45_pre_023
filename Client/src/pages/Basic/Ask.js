import Discription from '../../components/Ask/Discription';
import Title from '../../components/Ask/Title';
import DetailProbelm from '../../components/Ask/DetailProbelm';
import Expect from '../../components/Ask/Expect';
import Tags from '../../components/Ask/Tags';
import Review from '../../components/Ask/Review';
import DiscardBtn from '../../components/Ask/button/DiscardBtn';
import { useSelector } from 'react-redux';
import SubmitBtn from '../../components/Ask/button/SubmitBtn';

export default function Ask() {
  const Next = useSelector((state) => state.tipbox.position);

  return (
    <div className="flex flex-row min-w-full bg-gray-100">
      <div className="grow-2"></div>
      <div className="grow-5 flex flex-col">
        <div className="my-11 font-semibold text-2xl">
          Ask a public question
        </div>
        <Discription />
        <Title />
        <DetailProbelm />
        <Expect />
        <Tags />
        <Review />
        <div className='flex flex-row mt-1 mb-20'>
          {Next === 6 && <SubmitBtn />}
          <DiscardBtn />
        </div>
      </div>
    </div>
  );
}
