import React, { useEffect, useState } from 'react';
import { CountInfo, StatisticsApi } from '@api/api';
import Spinner from '@components/common/Spinner';

export default function HomePage() {
  const [countInfo, setCountInfo] = useState<CountInfo | undefined>();

  useEffect(() => {
    new StatisticsApi().statisticControllerCounts().then(response => {
      setCountInfo(response.data.data);
    });
  }, []);

  const validData =
    countInfo == null
      ? undefined
      : countInfo.customerCount >= 300 && countInfo.shopCount >= 30 && countInfo.productCount >= 30003;

  return (
    <>
      <div className="col-span-full overflow-x-auto bg-white p-5 border rounded-lg shadow-md flex flex-col">
        <span className="mb-4 font-semibold text-lg text-gray-900">Thống kê</span>
        {countInfo == null && <Spinner />}
        {countInfo != null && (
          <>
            <ul>
              <li>
                <span>{`Khách hàng: ${countInfo.customerCount}/300`}</span>
              </li>
              <li>
                <span>{`Cửa hàng: ${countInfo.shopCount}/30`}</span>
              </li>
              <li>
                <span>{`Sản phẩm: ${countInfo.productCount}/30003`}</span>
              </li>
            </ul>
            {validData != null && (
              <span className={`${validData ? 'text-green-600' : 'text-red-600'}`}>
                {validData ? 'Đã đủ dữ liệu' : 'Thiếu dữ liệu'}
              </span>
            )}
          </>
        )}
      </div>
    </>
  );
}
