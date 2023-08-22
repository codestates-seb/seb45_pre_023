import Discription from '../../components/Ask/Discription';
import Title from '../../components/Ask/Title';
import DetailProbelm from '../../components/Ask/DetailProbelm';
import Expect from '../../components/Ask/Expect';
import Tags from '../../components/Ask/Tags';
import Review from '../../components/Ask/Review';
import DiscardBtn from '../../components/Ask/button/DiscardBtn';
import { useDispatch, useSelector } from 'react-redux';
import SubmitBtn from '../../components/Ask/button/SubmitBtn';
import { useEffect } from 'react';
import axios from 'axios';
import { setTagList } from '../../redux/createSlice/AskSlice';

export default function Ask() {
  const dispatch = useDispatch();
  const Next = useSelector((state) => state.tipbox.nextbtn);

  const getTags = () => {
    return axios
      .get(
        'http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/tags'
      )
      .then((res) => dispatch(setTagList(res.data.data)))
      .catch((err) => console.log(err));
  };

  useEffect(() => {
    getTags();
  }, []);

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
        <div className="flex flex-row mt-1 mb-20">
          {Next === 6 && <SubmitBtn />}
          <DiscardBtn />
        </div>
      </div>
    </div>
  );
}
