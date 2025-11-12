import React, { useCallback, useEffect } from 'react';

export interface ImageModalProps {
  src: string;
  modalOpen: boolean;
  setModalOpen: (modelOpen: boolean) => void;
}

const ImageModal = ({ src, modalOpen, setModalOpen }: ImageModalProps) => {
  const closeModal = useCallback(() => {
    setModalOpen(false);
  }, [setModalOpen]);

  useEffect(() => {
    const clickHandler = ({}: MouseEvent) => {
      if (!modalOpen) return;
      closeModal();
    };
    document.addEventListener('mousedown', clickHandler);

    const keyHandler = ({ keyCode }: KeyboardEvent) => {
      if (!modalOpen || keyCode !== 27) return;
      closeModal();
    };
    document.addEventListener('keydown', keyHandler);

    return () => {
      document.removeEventListener('mousedown', clickHandler);
      document.removeEventListener('keydown', keyHandler);
    };
  }, [closeModal, modalOpen, setModalOpen]);

  return (
    <>
      {modalOpen && (
        <div className="fixed inset-0 z-60 w-full bg-gray-800 bg-opacity-50">
          <div className="flex items-center justify-center">
            <img
              className="object-contain max-w-screen max-h-screen h-auto object-center"
              src={src}
              alt="Full Size Image"
            />
          </div>
        </div>
      )}
    </>
  );
};

export default ImageModal;
