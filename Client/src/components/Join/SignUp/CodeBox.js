import { useSelector, useDispatch } from 'react-redux';
import { code } from '../../../redux/createSlice/SignUpInfoSlice';
import { errmsg } from '../../../redux/createSlice/ErrMsgSlice';
import axios from 'axios';

export default function CodeBox() {
  const SignUpInfo = useSelector((state) => state.signupinfo.value);
  const Code = useSelector((state) => state.signupinfo.code);
  const dispatch = useDispatch();

  const handleVerifyCode = () => {
    console.log({ email: SignUpInfo.email, code: Code });
    return axios
      .post(
        'http://ec2-43-201-249-199.ap-northeast-2.compute.amazonaws.com/members/email/confirm',
        { email: SignUpInfo.email, code: Code }
      )
      .then((res) => {
        if (res.data.data) {
          alert('성공적으로 이메일 인증이 완료되었습니다');
          dispatch(errmsg('Verified with a valid code.'));
        } else {
          alert('이메일 인증에 실패했습니다.');
          dispatch(errmsg(`Invalid authorization code.`));
        }
      })
      .catch((err) => {
        if (err.response.data.code === 400) {
          dispatch(errmsg('Please enter authorization code'));
        } else {
          alert(`이메일 인증에 실패했습니다. ${err.response.data.code}`);
        }
      });
  };

  return (
    <div className="flex flex-col justify-start items-center mt-4 w-68 bg-gray-100 rounded-md">
      <span className="mt-3 ml-2 w-60 text-left text-md font-semibold">
        Code
      </span>
      <div className="flex felx-row justify-between items-center w-60 mb-3">
        <input
          className="my-1 w-28 h-9 pl-2 border-2 border-solid border-gray rounded-md text-sm text-center"
          type="text"
          onChange={(e) => dispatch(code(e.target.value))}
        ></input>
        <button
          className="w-28 h-9 bg-sky-500 hover:bg-sky-600 text-sm text-white text-center rounded-md"
          onClick={handleVerifyCode}
        >
          Verify
        </button>
      </div>
    </div>
  );
}
