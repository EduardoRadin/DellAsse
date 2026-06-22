/**
 * Extrai a mensagem amigável de uma resposta ApiError do Axios.
 */
export function getApiErrorMessage(error, fallback = 'Ocorreu um erro inesperado.') {
  const data = error?.response?.data;

  if (!data) {
    return error?.message || fallback;
  }

  if (data.code === 'VALIDATION_ERROR' && data.fieldErrors) {
    const fields = Object.values(data.fieldErrors);
    if (fields.length > 0) {
      return fields.join(' · ');
    }
  }

  return data.message || fallback;
}
