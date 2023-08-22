import axios from 'axios';
import { useDispatch, useSelector } from 'react-redux';
import { useNavigate } from 'react-router-dom';
import { RouteConst } from '../../../Interface/RouteConst';
import { initValue } from '../../../redux/createSlice/AskSlice';

export default function SubmitBtn() {
  const navigate = useNavigate();
  const dispatch = useDispatch();
  const AskData = useSelector((state) => state.ask.value);
  const OAuthtoken = useSelector((state) => state.oauth.token);
  const Logintoken = useSelector((state) => state.logininfo.token);
  const accesstoken = OAuthtoken ? OAuthtoken : Logintoken;

  const headers = {
    Authorization: accesstoken,
  };

  const handleSubmit = () => {
    return axios
      .post(
        'http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/questions',
        AskData,
        { headers }
      )
      .then((res) => {
        alert('성공적으로 질문이 작성되었습니다.');
        navigate(RouteConst.Main);
        dispatch(initValue());
      })
      .catch((err) => {
        if (err.response.data.code === 400) {
          alert(`모든 정보를 입력해주세요. ${err.response.data.code}`);
        } else {
          alert(`질문 제출에 실패했습니다. ${err.response.data.code}`);
        }
      });
  };

  return (
    <button
      className="w-34 h-9 mx-1 bg-sky-500 hover:bg-sky-600 rounded-md text-xs text-white"
      onClick={handleSubmit}
    >
      Submit your question
    </button>
  );
}