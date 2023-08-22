import { useDispatch, useSelector } from 'react-redux';
import axios from 'axios';
import {
  findpassword,
  findmodemodal,
  findinit,
  findmsg,
} from '../../../../redux/createSlice/FindInfoSlice';

export default function FindPassword() {
  const dispatch = useDispatch();
  const FindInfo = useSelector((state) => state.findinfo);

  const handleNewPW = () => {
    if (!FindInfo.password) {
      return dispatch(findmsg('Please enter a new password.'));
    }

    return axios
      .patch(
        'http://ec2-3-39-228-109.ap-northeast-2.compute.amazonaws.com/auth/password',
        { email: FindInfo.email, password: FindInfo.password }
      )
      .then((res) => {
        alert('비밀번호 변경이 완료되었습니다.');
        dispatch(findmodemodal());
        dispatch(findinit());
      })
      .catch((err) => {
        dispatch(
          findmsg(`Please enter a vaild password. (${err.response.data.code})`)
        );
        console.log(err.response);
      });
  };

  return (
    <div>
      <div className="flex flex-col justify-center items-center">
        <span className="mt-2 pl-1 w-72 text-left text-md font-semibold">
          New password
        </span>
        <input
          className="my-1 w-72 h-9 pl-2 border-2 border-solid border-gray rounded-md text-sm"
          type="password"
          value={FindInfo.password}
          onChange={(e) => dispatch(findpassword(e.target.value))}
        />
        <span className="my-1 pl-1 w-72 text-xs text-gray-400">
          Passwords must contain at least nine characters, including at least 1
          special character and 1 number.
        </span>
      </div>

      <button
        className="w-72 h-9 mt-2 mb-3 bg-sky-500 hover:bg-sky-400 text-sm text-white text-center rounded-md"
        onClick={handleNewPW}
      >
        Submit new password
      </button>
    </div>
  );
}
