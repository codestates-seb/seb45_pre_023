import { Link } from 'react-router-dom';
import  getTimeDifference  from '../Question/Time';

export default function QuestionData( {el} ) {
    const inputTime = el.createdDate;
    const hoursAgo = getTimeDifference(inputTime);

    return(
        <>
        <div  className="w-full flex justify-around items-center border-t border-t-[#9e9e9e] border-b-1 border-b-[#9e9e9e]">
          {/* 투표수, 답변수, 조회수  */}
          <div className="w-[150px] h-[80px] flex flex-col justify-between items-center ml-6">
            <p>{el.recommend} votes</p>
            <p className=" text-[#90A4AE]">{el.answerCount} answer</p>
            <p className="text-[#90A4AE]">{el.views} views</p>
          </div>

          {/* 컨텐츠 */}
          <div  className="w-[900px] h-[150px] flex flex-col justify-around ">
            <Link to={`/questions/${el.questionId}`}>
              <p className="text-[#6179c9] text-xl hover:text-[#afdcf7]">
                {el.title}
              </p>
            </Link>
            <p className="text-[#616161]">{el.detail}</p>
            {/* 태그, 유저, 생성날짜 */}
            <div className="w-full flex justify-between">
              <div className="mr-4">
                {el.tags.map((tag, index) => (
                  <button
                    key={index}
                    className="w-[100px] bg-[#afdcf7b4] rounded-full mr-4 text-[#95a6dd] hover:translate-x-1 hover:translate-y-1 leading-2"
                  >
                    {tag.tagName}
                  </button>
                ))}
              </div>
              <div className="flex justify-between">
                <img src="https://i.pinimg.com/564x/40/dc/af/40dcaf3511a13efabcfc4d741a632324.jpg" alt='짱구 이미지' className='w-[20px] h-[20px]' />
                <p className="ml-4 text-sm text-[#4FC3F7]">{el.member.nickname}</p>
                <p className="ml-6 text-sm text-[#9e9e9e]">{hoursAgo} hour ago</p>
              </div>
            </div>
          </div>
        </div>
        </>
    );
}